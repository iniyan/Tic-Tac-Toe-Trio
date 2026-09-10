package `in`.iniyan.ttt.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val winner: String, // "X", "O", "N", or "draw"
    val difficulty: String,
    val moveCount: Int,
    val moveSequence: String, // JSON serialized moves
    val winPattern: String?, // JSON serialized win pattern
    val seriesId: Long
)
