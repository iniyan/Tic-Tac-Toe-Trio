package `in`.iniyan.tictactoetrio.data.dao

import androidx.room.*
import `in`.iniyan.tictactoetrio.data.entity.PlayerStats
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerStatsDao {
    @Query("SELECT * FROM player_stats WHERE id = 1")
    fun getStats(): Flow<PlayerStats?>

    @Query("SELECT * FROM player_stats WHERE id = 1")
    suspend fun getStatsOnce(): PlayerStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stats: PlayerStats)

    @Query("UPDATE player_stats SET totalXP = totalXP + :xp WHERE id = 1")
    suspend fun addXP(xp: Int)

    @Query("UPDATE player_stats SET hintTokens = hintTokens + :amount WHERE id = 1")
    suspend fun addHintTokens(amount: Int)

    @Query("UPDATE player_stats SET hintTokens = hintTokens - 1 WHERE id = 1 AND hintTokens > 0")
    suspend fun useHintToken()
}
