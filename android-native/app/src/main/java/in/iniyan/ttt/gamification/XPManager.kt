package `in`.iniyan.ttt.gamification

import `in`.iniyan.ttt.data.entity.PlayerStats
import `in`.iniyan.ttt.model.Difficulty

object XPManager {

    enum class PlayerLevel(val title: String, val minXP: Int) {
        NOVICE("Novice", 0),
        TACTICIAN("Tactician", 500),
        STRATEGIST("Strategist", 1000),
        GRANDMASTER("Grandmaster", 1500),
        LEGENDARY("Legendary", 2000);

        companion object {
            fun fromXP(xp: Int): PlayerLevel {
                return entries.lastOrNull { xp >= it.minXP } ?: NOVICE
            }

            fun nextLevel(current: PlayerLevel): PlayerLevel? {
                val idx = entries.indexOf(current)
                return if (idx < entries.size - 1) entries[idx + 1] else null
            }
        }
    }

    fun calculateXP(isWin: Boolean, isDraw: Boolean, difficulty: Difficulty): Int {
        val baseXP = when {
            isWin -> 100
            isDraw -> 40
            else -> 15
        }
        val multiplier = when (difficulty) {
            Difficulty.EASY -> 1.0f
            Difficulty.MEDIUM -> 1.5f
            Difficulty.HARD -> 2.0f
        }
        return (baseXP * multiplier).toInt()
    }

    fun getLevelFromXP(xp: Int): PlayerLevel = PlayerLevel.fromXP(xp)

    fun getXPProgressInLevel(xp: Int): Float {
        val currentLevel = PlayerLevel.fromXP(xp)
        val nextLevel = PlayerLevel.nextLevel(currentLevel) ?: return 1f
        val xpInLevel = xp - currentLevel.minXP
        val xpNeeded = nextLevel.minXP - currentLevel.minXP
        return (xpInLevel.toFloat() / xpNeeded).coerceIn(0f, 1f)
    }

    fun getXPToNextLevel(xp: Int): Int {
        val currentLevel = PlayerLevel.fromXP(xp)
        val nextLevel = PlayerLevel.nextLevel(currentLevel) ?: return 0
        return nextLevel.minXP - xp
    }

    fun didLevelUp(oldXP: Int, newXP: Int): Boolean {
        return PlayerLevel.fromXP(oldXP) != PlayerLevel.fromXP(newXP)
    }
}
