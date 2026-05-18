package `in`.iniyan.ttt.gamification

import `in`.iniyan.ttt.gamification.XPManager.PlayerLevel

data class SkinDefinition(
    val id: String,
    val name: String,
    val type: String, // "symbol" or "board"
    val unlockLevel: PlayerLevel,
    val description: String
)

object SkinManager {

    val symbolSkins = listOf(
        SkinDefinition("default_x", "Default X", "symbol", PlayerLevel.NOVICE, "The classic X marker"),
        SkinDefinition("flame_x", "Flame X", "symbol", PlayerLevel.TACTICIAN, "A fiery X that burns the board"),
        SkinDefinition("neon_x", "Neon X", "symbol", PlayerLevel.GRANDMASTER, "A glowing neon X"),
        SkinDefinition("crown_x", "Crown X", "symbol", PlayerLevel.LEGENDARY, "A royal crowned X")
    )

    val boardSkins = listOf(
        SkinDefinition("classic", "Classic", "board", PlayerLevel.NOVICE, "Clean white board"),
        SkinDefinition("dark_slate", "Dark Slate", "board", PlayerLevel.TACTICIAN, "Sleek dark theme"),
        SkinDefinition("paper", "Paper", "board", PlayerLevel.STRATEGIST, "Textured paper look"),
        SkinDefinition("neon_grid", "Neon Grid", "board", PlayerLevel.GRANDMASTER, "Cyberpunk neon grid")
    )

    val allSkins = symbolSkins + boardSkins

    fun getUnlockableSkins(level: PlayerLevel): List<SkinDefinition> {
        return allSkins.filter { it.unlockLevel.minXP <= level.minXP }
    }

    fun isLocked(skin: SkinDefinition, currentLevel: PlayerLevel): Boolean {
        return skin.unlockLevel.minXP > currentLevel.minXP
    }
}
