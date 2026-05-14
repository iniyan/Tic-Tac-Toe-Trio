package `in`.iniyan.tictactoetrio.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unlocked_skins")
data class UnlockedSkin(
    @PrimaryKey val skinId: String,
    val type: String, // "symbol" or "board"
    val isActive: Boolean = false
)
