package `in`.iniyan.tictactoetrio.gamification

import kotlin.random.Random

sealed class MysteryReward(val displayName: String) {
    data class ExtraXP(val amount: Int) : MysteryReward("$amount Bonus XP")
    data class HintTokens(val count: Int) : MysteryReward("$count Hint Token${if (count > 1) "s" else ""}")
    data class TempSkin(val skinId: String, val skinName: String) : MysteryReward("$skinName (24hr)")
    data object Nothing : MysteryReward("Better luck next time!")
}

object MysteryBox {

    fun shouldShowBox(gamesPlayed: Int, lastBoxGame: Int): Boolean {
        val gamesSinceBox = gamesPlayed - lastBoxGame
        val threshold = Random.nextInt(4, 8) // Random between 4-7 games
        return gamesSinceBox >= threshold
    }

    fun generateReward(): MysteryReward {
        val roll = Random.nextFloat()
        return when {
            roll < 0.35f -> MysteryReward.ExtraXP(Random.nextInt(50, 201))
            roll < 0.60f -> MysteryReward.HintTokens(Random.nextInt(1, 3))
            roll < 0.85f -> {
                val skins = listOf(
                    "neon_x" to "Neon X",
                    "flame_x" to "Flame X",
                    "crown_x" to "Crown X"
                )
                val (id, name) = skins.random()
                MysteryReward.TempSkin(id, name)
            }
            else -> MysteryReward.Nothing
        }
    }
}
