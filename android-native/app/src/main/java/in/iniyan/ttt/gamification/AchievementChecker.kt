package `in`.iniyan.ttt.gamification

import `in`.iniyan.ttt.data.dao.AchievementDao
import `in`.iniyan.ttt.data.entity.PlayerStats

data class GameResult(
    val winner: String, // "X", "O", "N", "draw"
    val difficulty: String,
    val moveCount: Int,
    val winPatternType: String?, // "diagonal", "2x2", "horizontal", "vertical", null
    val seriesStats: Map<String, Int>, // current series scores
    val maxGames: Int,
    val currentGame: Int,
    val wasBehindInSeries: Boolean,
    val isPowerPlayerMode: Boolean = false
)

class AchievementChecker(private val achievementDao: AchievementDao) {

    suspend fun checkAfterGame(stats: PlayerStats, result: GameResult): List<String> {
        val unlocked = mutableListOf<String>()

        fun tryUnlock(id: String): Boolean {
            return true // will be checked via dao
        }

        // First Blood
        if (stats.totalWins >= 1) {
            if (achievementDao.unlock("first_blood") > 0) unlocked.add("First Blood")
        }

        // Triple Threat
        if (stats.currentStreak >= 3) {
            if (achievementDao.unlock("triple_threat") > 0) unlocked.add("Triple Threat")
        }

        // On Fire
        if (stats.currentStreak >= 5) {
            if (achievementDao.unlock("five_streak") > 0) unlocked.add("On Fire")
        }

        // Untouchable - 5-series without losing
        if (result.maxGames == 5 && result.currentGame == result.maxGames) {
            val xWins = result.seriesStats["X"] ?: 0
            val oWins = result.seriesStats["O"] ?: 0
            val nWins = result.seriesStats["N"] ?: 0
            if (oWins == 0 && nWins == 0 && xWins > 0) {
                if (achievementDao.unlock("untouchable") > 0) unlocked.add("Untouchable")
            }
        }

        // Speed Demon
        if (result.winner == "X" && result.moveCount < 10) {
            if (achievementDao.unlock("speed_demon") > 0) unlocked.add("Speed Demon")
        }

        // Comeback Kid
        if (result.winner == "X" && result.wasBehindInSeries) {
            if (achievementDao.unlock("comeback_kid") > 0) unlocked.add("Comeback Kid")
        }

        // Exterminator
        if (stats.hardWins >= 10) {
            if (achievementDao.unlock("exterminator") > 0) unlocked.add("Exterminator")
        }

        // Strategist - 2x2 win
        if (result.winner == "X" && result.winPatternType == "2x2") {
            if (achievementDao.unlock("strategist") > 0) unlocked.add("Strategist")
        }

        // Diagonal Master
        if (stats.gamesWonWithDiagonal >= 5) {
            if (achievementDao.unlock("diagonal_master") > 0) unlocked.add("Diagonal Master")
        }

        // Marathon Runner
        if (stats.totalGames >= 100) {
            if (achievementDao.unlock("marathon_runner") > 0) unlocked.add("Marathon Runner")
        }

        // Daily Warrior
        if (stats.consecutiveDaysPlayed >= 7) {
            if (achievementDao.unlock("daily_warrior") > 0) unlocked.add("Daily Warrior")
        }

        // Perfect Series
        if (result.maxGames == 10 && result.currentGame == result.maxGames) {
            val xWins = result.seriesStats["X"] ?: 0
            if (xWins == 10) {
                if (achievementDao.unlock("perfect_series") > 0) unlocked.add("Perfect Series")
            }
        }

        // Lone Wolf
        if (result.winner == "X") {
            val oWins = result.seriesStats["O"] ?: 0
            val nWins = result.seriesStats["N"] ?: 0
            if (oWins >= 3 && nWins >= 3) {
                if (achievementDao.unlock("lone_wolf") > 0) unlocked.add("Lone Wolf")
            }
        }

        // Centurion
        if (stats.totalWins >= 100) {
            if (achievementDao.unlock("centurion") > 0) unlocked.add("Centurion")
        }

        // XP Hunter
        if (stats.totalXP >= 1000) {
            if (achievementDao.unlock("xp_hunter") > 0) unlocked.add("XP Hunter")
        }

        // Grandmaster
        if (XPManager.getLevelFromXP(stats.totalXP) == XPManager.PlayerLevel.GRANDMASTER) {
            if (achievementDao.unlock("grandmaster") > 0) unlocked.add("Grandmaster")
        }

        // Legendary
        if (XPManager.getLevelFromXP(stats.totalXP) == XPManager.PlayerLevel.LEGENDARY) {
            if (achievementDao.unlock("legendary") > 0) unlocked.add("Legendary")
        }

        // Shield Bearer
        if (stats.streakShieldCount >= 1) {
            if (achievementDao.unlock("shield_bearer") > 0) unlocked.add("Shield Bearer")
        }

        // Outnumbered
        if (result.isPowerPlayerMode && result.winner == "X" && result.currentGame == result.maxGames) {
            val xWins = result.seriesStats["X"] ?: 0
            val maxOther = maxOf(result.seriesStats["O"] ?: 0, result.seriesStats["N"] ?: 0)
            if (xWins > maxOther) {
                if (achievementDao.unlock("outnumbered") > 0) unlocked.add("Outnumbered")
            }
        }

        // Dedicated Player (14 daily streak)
        if (stats.dailyChallengeStreak >= 14) {
            if (achievementDao.unlock("daily_streak_14") > 0) unlocked.add("Dedicated Player")
        }

        return unlocked
    }
}
