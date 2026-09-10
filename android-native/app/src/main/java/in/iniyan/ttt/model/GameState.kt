package `in`.iniyan.ttt.model

import androidx.compose.ui.graphics.Color
import `in`.iniyan.ttt.gamification.MysteryReward
import `in`.iniyan.ttt.gamification.XPManager

enum class Player(val symbol: String, val displayName: String, val color: Color, val colorEnd: Color) {
    X("X", "Player", Color(0xFF48BB78), Color(0xFF38A169)),
    O("O", "Blocker", Color(0xFFED8936), Color(0xFFDD6B20)),
    N("N", "Hunter", Color(0xFF4299E1), Color(0xFF3182CE));

    companion object {
        fun fromSymbol(symbol: String): Player? = entries.find { it.symbol == symbol }
    }
}

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard")
}

data class WinResult(
    val winner: String? = null,
    val pattern: List<Int>? = null
)

enum class Screen {
    SETUP, GAME
}

data class PostGameInfo(
    val xpEarned: Int = 0,
    val newLevel: XPManager.PlayerLevel? = null,
    val leveledUp: Boolean = false,
    val achievementsUnlocked: List<String> = emptyList(),
    val mysteryReward: MysteryReward? = null,
    val showMysteryBox: Boolean = false
)

data class GameState(
    val board: List<String> = List(25) { "" },
    val currentPlayer: Player = Player.X,
    val seriesStats: Map<Player, Int> = mapOf(Player.X to 0, Player.O to 0, Player.N to 0),
    val currentGame: Int = 1,
    val maxGames: Int = 10,
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val playerName: String = "Player",
    val winner: String? = null,
    val winningPattern: List<Int>? = null,
    val screen: Screen = Screen.SETUP,
    val isSeriesOver: Boolean = false,
    val showRulesDialog: Boolean = false,
    val showStatsDialog: Boolean = false,
    val showSurrenderDialog: Boolean = false,
    val showPostGameScreen: Boolean = false,
    val showAchievementsScreen: Boolean = false,
    val showSkinsScreen: Boolean = false,
    val statusMessage: String = "",
    val isBotThinking: Boolean = false,
    val moveCount: Int = 0,
    val moveHistory: List<Pair<Int, String>> = emptyList(),
    // Gamification state
    val totalXP: Int = 0,
    val currentLevel: XPManager.PlayerLevel = XPManager.PlayerLevel.NOVICE,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val hintTokens: Int = 3,
    val hasShield: Boolean = false,
    val postGameInfo: PostGameInfo = PostGameInfo(),
    val highlightedHintCell: Int? = null,
    val isPowerPlayerMode: Boolean = false,
    val wasBehindInSeries: Boolean = false,
    val dailyChallengeCompleted: Boolean = false,
    val dailyChallengeExpiresAt: Long = 0L
)
