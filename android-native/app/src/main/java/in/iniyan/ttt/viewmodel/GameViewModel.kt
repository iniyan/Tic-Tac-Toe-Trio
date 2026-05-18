package `in`.iniyan.ttt.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.iniyan.ttt.data.database.AppDatabase
import `in`.iniyan.ttt.data.entity.GameRecord
import `in`.iniyan.ttt.data.entity.PlayerStats
import `in`.iniyan.ttt.gamification.*
import `in`.iniyan.ttt.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val statsDao = db.playerStatsDao()
    private val achievementDao = db.achievementDao()
    private val gameRecordDao = db.gameRecordDao()
    private val skinDao = db.unlockedSkinDao()
    private val dailyChallengeDao = db.dailyChallengeDao()
    private val achievementChecker = AchievementChecker(achievementDao)

    var state by mutableStateOf(GameState())
        private set

    private var seriesId = System.currentTimeMillis()
    private var lastMysteryBoxGame = 0
    private var totalGamesThisSession = 0

    init {
        viewModelScope.launch {
            // Initialize achievements
            achievementDao.insertAll(AchievementDefinitions.allAchievements())

            // Initialize player stats if first launch
            val stats = statsDao.getStatsOnce()
            if (stats == null) {
                statsDao.upsert(PlayerStats())
            }

            // Load persisted stats into state
            loadStatsIntoState()
        }
    }

    private suspend fun loadStatsIntoState() {
        val stats = statsDao.getStatsOnce() ?: PlayerStats()
        state = state.copy(
            totalXP = stats.totalXP,
            currentLevel = XPManager.getLevelFromXP(stats.totalXP),
            currentStreak = stats.currentStreak,
            bestStreak = stats.bestStreak,
            hintTokens = stats.hintTokens,
            hasShield = stats.hasShield
        )

        // Check daily challenge
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val challenge = dailyChallengeDao.getChallengeForDate(today)
        if (challenge == null) {
            val newChallenge = DailyChallengeGenerator.getChallengeForDate()
            dailyChallengeDao.upsert(newChallenge)
            state = state.copy(
                dailyChallengeCompleted = false,
                dailyChallengeExpiresAt = newChallenge.expiresAt
            )
        } else {
            state = state.copy(
                dailyChallengeCompleted = challenge.isCompleted,
                dailyChallengeExpiresAt = challenge.expiresAt
            )
        }
    }

    fun updatePlayerName(name: String) {
        state = state.copy(playerName = name)
    }

    fun updateDifficulty(difficulty: Difficulty) {
        state = state.copy(difficulty = difficulty)
    }

    fun updateMaxGames(maxGames: Int) {
        state = state.copy(maxGames = maxGames)
    }

    fun togglePowerPlayerMode() {
        if (state.currentLevel.ordinal >= XPManager.PlayerLevel.STRATEGIST.ordinal) {
            state = state.copy(isPowerPlayerMode = !state.isPowerPlayerMode)
        }
    }

    fun startNewSeries() {
        seriesId = System.currentTimeMillis()
        state = state.copy(
            seriesStats = mapOf(Player.X to 0, Player.O to 0, Player.N to 0),
            currentGame = 1,
            isSeriesOver = false,
            screen = Screen.GAME,
            showStatsDialog = false,
            wasBehindInSeries = false
        )
        startNewGame()

        // If power player mode, give O a head start
        if (state.isPowerPlayerMode) {
            viewModelScope.launch {
                delay(300)
                val move = BotLogic.botMove(state.board, Player.O, state.difficulty)
                if (move != null) {
                    val newBoard = GameLogic.makeMove(state.board, move, Player.O)
                    if (newBoard != null) {
                        state = state.copy(board = newBoard)
                    }
                }
            }
        }
    }

    private fun startNewGame() {
        state = state.copy(
            board = List(25) { "" },
            currentPlayer = Player.X,
            winner = null,
            winningPattern = null,
            statusMessage = "",
            isBotThinking = false,
            moveCount = 0,
            moveHistory = emptyList(),
            highlightedHintCell = null,
            showPostGameScreen = false
        )
    }

    fun playTurn(index: Int) {
        if (state.winner != null || state.isBotThinking) return
        if (state.currentGame > state.maxGames) return
        if (state.board[index].isNotEmpty()) return

        val newBoard = GameLogic.makeMove(state.board, index, state.currentPlayer) ?: return
        val newMoveCount = state.moveCount + 1
        val newHistory = state.moveHistory + (index to state.currentPlayer.symbol)

        state = state.copy(
            board = newBoard,
            moveCount = newMoveCount,
            moveHistory = newHistory,
            highlightedHintCell = null
        )

        val result = GameLogic.checkWinner(newBoard)
        if (result.winner != null) {
            handleWin(result)
        } else {
            val nextPlayer = getNextPlayer(state.currentPlayer)
            state = state.copy(currentPlayer = nextPlayer)
            if (nextPlayer != Player.X) {
                triggerBotMove()
            }
        }
    }

    private fun triggerBotMove() {
        state = state.copy(isBotThinking = true)
        viewModelScope.launch {
            delay(400)
            val move = BotLogic.botMove(state.board, state.currentPlayer, state.difficulty)
            if (move != null) {
                val newBoard = GameLogic.makeMove(state.board, move, state.currentPlayer)
                if (newBoard != null) {
                    val newMoveCount = state.moveCount + 1
                    val newHistory = state.moveHistory + (move to state.currentPlayer.symbol)
                    state = state.copy(
                        board = newBoard,
                        isBotThinking = false,
                        moveCount = newMoveCount,
                        moveHistory = newHistory
                    )

                    val result = GameLogic.checkWinner(newBoard)
                    if (result.winner != null) {
                        handleWin(result)
                    } else {
                        val nextPlayer = getNextPlayer(state.currentPlayer)
                        state = state.copy(currentPlayer = nextPlayer)
                        if (nextPlayer != Player.X) {
                            triggerBotMove()
                        }
                    }
                }
            } else {
                state = state.copy(isBotThinking = false)
            }
        }
    }

    private fun handleWin(result: WinResult) {
        val isPlayerWin = result.winner == "X"
        val isDraw = result.winner == "draw"
        val isPlayerLoss = !isPlayerWin && !isDraw

        if (isDraw) {
            state = state.copy(
                winner = "draw",
                statusMessage = "It's a draw!"
            )
        } else {
            val winnerPlayer = Player.fromSymbol(result.winner!!)!!
            val newStats = state.seriesStats.toMutableMap()
            newStats[winnerPlayer] = (newStats[winnerPlayer] ?: 0) + 1
            val displayName = if (winnerPlayer == Player.X) state.playerName else winnerPlayer.displayName

            // Track if player was behind
            val xWins = if (winnerPlayer == Player.X) (newStats[Player.X] ?: 0) else (state.seriesStats[Player.X] ?: 0)
            val maxOther = maxOf(newStats[Player.O] ?: 0, newStats[Player.N] ?: 0)
            val wasBehind = state.wasBehindInSeries || (isPlayerWin && xWins <= maxOther && state.currentGame > 1)

            state = state.copy(
                winner = result.winner,
                winningPattern = result.pattern,
                seriesStats = newStats,
                statusMessage = "$displayName wins!",
                wasBehindInSeries = wasBehind
            )
        }

        totalGamesThisSession++

        // Process gamification
        viewModelScope.launch {
            processGameEnd(isPlayerWin, isDraw, isPlayerLoss, result)

            delay(3000)
            if (state.currentGame >= state.maxGames) {
                endSeries()
            } else {
                state = state.copy(currentGame = state.currentGame + 1)
                startNewGame()
            }
        }
    }

    private suspend fun processGameEnd(isWin: Boolean, isDraw: Boolean, isLoss: Boolean, winResult: WinResult) {
        val stats = statsDao.getStatsOnce() ?: PlayerStats()

        // Calculate XP
        val xpEarned = XPManager.calculateXP(isWin, isDraw, state.difficulty)
        val oldXP = stats.totalXP
        val newXP = oldXP + xpEarned
        val leveledUp = XPManager.didLevelUp(oldXP, newXP)
        val newLevel = XPManager.getLevelFromXP(newXP)

        // Update streak
        var newStreak = stats.currentStreak
        var newBestStreak = stats.bestStreak
        var newShield = stats.hasShield
        var shieldCount = stats.streakShieldCount

        if (isWin) {
            newStreak++
            if (newStreak > newBestStreak) newBestStreak = newStreak
            // Earn shield every 3 wins
            if (newStreak % 3 == 0 && !newShield) {
                newShield = true
                shieldCount++
            }
        } else if (isLoss) {
            if (newShield) {
                newShield = false // Shield consumed
            } else {
                newStreak = 0
            }
        }

        // Determine win pattern type
        val winPatternType = if (isWin && winResult.pattern != null) {
            determinePatternType(winResult.pattern)
        } else null

        // Update daily tracking
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val consecutiveDays = if (stats.lastPlayedDate == today) {
            stats.consecutiveDaysPlayed
        } else {
            val yesterday = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -1)
            }
            val yesterdayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(yesterday.time)
            if (stats.lastPlayedDate == yesterdayStr) stats.consecutiveDaysPlayed + 1 else 1
        }

        // Update stats
        val updatedStats = stats.copy(
            totalGames = stats.totalGames + 1,
            totalWins = stats.totalWins + if (isWin) 1 else 0,
            totalLosses = stats.totalLosses + if (isLoss) 1 else 0,
            totalDraws = stats.totalDraws + if (isDraw) 1 else 0,
            currentStreak = newStreak,
            bestStreak = newBestStreak,
            totalXP = newXP,
            level = newLevel.ordinal + 1,
            hasShield = newShield,
            streakShieldCount = shieldCount,
            hardWins = stats.hardWins + if (isWin && state.difficulty == Difficulty.HARD) 1 else 0,
            fastestWinMoves = if (isWin && (stats.fastestWinMoves == 0 || state.moveCount < stats.fastestWinMoves))
                state.moveCount else stats.fastestWinMoves,
            gamesWonWith2x2 = stats.gamesWonWith2x2 + if (winPatternType == "2x2") 1 else 0,
            gamesWonWithDiagonal = stats.gamesWonWithDiagonal + if (winPatternType == "diagonal") 1 else 0,
            lastPlayedDate = today,
            consecutiveDaysPlayed = consecutiveDays,
            gamesUntilMysteryBox = stats.gamesUntilMysteryBox - 1
        )
        statsDao.upsert(updatedStats)

        // Save game record
        gameRecordDao.insert(
            GameRecord(
                winner = state.winner ?: "draw",
                difficulty = state.difficulty.name,
                moveCount = state.moveCount,
                moveSequence = state.moveHistory.joinToString(";") { "${it.first}:${it.second}" },
                winPattern = winResult.pattern?.joinToString(","),
                seriesId = seriesId
            )
        )

        // Check achievements
        val gameResult = GameResult(
            winner = state.winner ?: "draw",
            difficulty = state.difficulty.name,
            moveCount = state.moveCount,
            winPatternType = winPatternType,
            seriesStats = mapOf(
                "X" to (state.seriesStats[Player.X] ?: 0),
                "O" to (state.seriesStats[Player.O] ?: 0),
                "N" to (state.seriesStats[Player.N] ?: 0)
            ),
            maxGames = state.maxGames,
            currentGame = state.currentGame,
            wasBehindInSeries = state.wasBehindInSeries,
            isPowerPlayerMode = state.isPowerPlayerMode
        )
        val unlockedAchievements = achievementChecker.checkAfterGame(updatedStats, gameResult)

        // Mystery box check
        val showMystery = MysteryBox.shouldShowBox(updatedStats.totalGames, lastMysteryBoxGame)
        val mysteryReward = if (showMystery) {
            lastMysteryBoxGame = updatedStats.totalGames
            val reward = MysteryBox.generateReward()
            // Apply mystery reward
            when (reward) {
                is MysteryReward.ExtraXP -> statsDao.addXP(reward.amount)
                is MysteryReward.HintTokens -> statsDao.addHintTokens(reward.count)
                is MysteryReward.TempSkin -> { /* Temp skin unlock handled by skin manager */ }
                is MysteryReward.Nothing -> { }
            }
            if (unlockedAchievements.isEmpty() || achievementDao.getById("mystery_opener")?.isUnlocked != true) {
                achievementDao.unlock("mystery_opener")
            }
            reward
        } else null

        // Add hint token on level up
        if (leveledUp) {
            statsDao.addHintTokens(1)
        }

        // Update UI state
        val refreshedStats = statsDao.getStatsOnce() ?: updatedStats
        state = state.copy(
            totalXP = refreshedStats.totalXP,
            currentLevel = XPManager.getLevelFromXP(refreshedStats.totalXP),
            currentStreak = refreshedStats.currentStreak,
            bestStreak = refreshedStats.bestStreak,
            hintTokens = refreshedStats.hintTokens,
            hasShield = refreshedStats.hasShield,
            postGameInfo = PostGameInfo(
                xpEarned = xpEarned,
                newLevel = if (leveledUp) newLevel else null,
                leveledUp = leveledUp,
                achievementsUnlocked = unlockedAchievements,
                mysteryReward = mysteryReward,
                showMysteryBox = showMystery
            ),
            showPostGameScreen = true
        )
    }

    private fun determinePatternType(pattern: List<Int>): String {
        if (pattern.size == 4) {
            // Check if 2x2
            val sorted = pattern.sorted()
            if (sorted.size == 4) {
                val diff01 = sorted[1] - sorted[0]
                val diff23 = sorted[3] - sorted[2]
                val diff02 = sorted[2] - sorted[0]
                if (diff01 == 1 && diff23 == 1 && diff02 == 5) return "2x2"
            }
            // Check diagonal
            val diffs = (1 until pattern.size).map { pattern[it] - pattern[it - 1] }
            if (diffs.all { it == 6 } || diffs.all { it == 4 }) return "diagonal"
            // Check horizontal
            if (diffs.all { it == 1 }) return "horizontal"
            // Check vertical
            if (diffs.all { it == 5 }) return "vertical"
        }
        return "unknown"
    }

    fun useHintToken() {
        if (state.hintTokens <= 0 || state.winner != null || state.currentPlayer != Player.X) return

        viewModelScope.launch {
            statsDao.useHintToken()
            // Find best move for player
            val bestMove = BotLogic.botMove(state.board, Player.X, Difficulty.HARD)
            if (bestMove != null) {
                state = state.copy(
                    hintTokens = state.hintTokens - 1,
                    highlightedHintCell = bestMove
                )
            }
        }
    }

    private fun endSeries() {
        state = state.copy(isSeriesOver = true, showStatsDialog = true)
    }

    fun onNewGame() {
        startNewGame()
    }

    fun requestAbandonSeries() {
        // Show surrender warning if player is ahead
        val xWins = state.seriesStats[Player.X] ?: 0
        val maxOther = maxOf(state.seriesStats[Player.O] ?: 0, state.seriesStats[Player.N] ?: 0)
        if (xWins > maxOther && state.currentGame > 1) {
            state = state.copy(showSurrenderDialog = true)
        } else {
            abandonSeries()
        }
    }

    fun confirmAbandonSeries() {
        state = state.copy(showSurrenderDialog = false)
        abandonSeries()
    }

    fun cancelAbandonSeries() {
        state = state.copy(showSurrenderDialog = false)
    }

    fun abandonSeries() {
        state = state.copy(
            screen = Screen.SETUP,
            isSeriesOver = false,
            showStatsDialog = false,
            showPostGameScreen = false
        )
    }

    fun showRules() {
        state = state.copy(showRulesDialog = true)
    }

    fun hideRules() {
        state = state.copy(showRulesDialog = false)
    }

    fun showAchievements() {
        state = state.copy(showAchievementsScreen = true)
    }

    fun hideAchievements() {
        state = state.copy(showAchievementsScreen = false)
    }

    fun showSkins() {
        state = state.copy(showSkinsScreen = true)
    }

    fun hideSkins() {
        state = state.copy(showSkinsScreen = false)
    }

    fun dismissPostGame() {
        state = state.copy(showPostGameScreen = false)
    }

    fun dismissStats() {
        state = state.copy(showStatsDialog = false, screen = Screen.SETUP)
    }

    fun getSeriesWinnerText(): String {
        val maxScore = state.seriesStats.values.max()
        val winners = state.seriesStats.filter { it.value == maxScore }.keys
        return if (winners.size > 1) {
            "Series Result: It's a draw!"
        } else {
            val winner = winners.first()
            val name = if (winner == Player.X) state.playerName else winner.displayName
            "Series Winner: $name"
        }
    }

    private fun getNextPlayer(player: Player): Player {
        val players = Player.entries
        val currentIndex = players.indexOf(player)
        return players[(currentIndex + 1) % players.size]
    }
}
