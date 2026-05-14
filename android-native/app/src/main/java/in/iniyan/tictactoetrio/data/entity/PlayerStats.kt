package `in`.iniyan.tictactoetrio.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats")
data class PlayerStats(
    @PrimaryKey val id: Int = 1,
    val totalGames: Int = 0,
    val totalWins: Int = 0,
    val totalLosses: Int = 0,
    val totalDraws: Int = 0,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val totalXP: Int = 0,
    val level: Int = 1,
    val hintTokens: Int = 3,
    val hasShield: Boolean = false,
    val gamesUntilMysteryBox: Int = 5,
    val dailyChallengeStreak: Int = 0,
    val lastPlayedDate: String = "",
    val consecutiveDaysPlayed: Int = 0,
    val hardWins: Int = 0,
    val fastestWinMoves: Int = 0,
    val totalMovesAllGames: Int = 0,
    val gamesWonWith2x2: Int = 0,
    val gamesWonWithDiagonal: Int = 0,
    val streakShieldCount: Int = 0
)
