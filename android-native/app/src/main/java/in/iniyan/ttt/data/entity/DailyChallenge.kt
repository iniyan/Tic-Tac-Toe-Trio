package `in`.iniyan.ttt.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_challenges")
data class DailyChallenge(
    @PrimaryKey val date: String,
    val boardState: String,
    val targetMoves: Int,
    val isCompleted: Boolean = false,
    val expiresAt: Long
)
