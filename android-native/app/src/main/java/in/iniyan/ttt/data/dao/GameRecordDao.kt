package `in`.iniyan.ttt.data.dao

import androidx.room.*
import `in`.iniyan.ttt.data.entity.GameRecord

@Dao
interface GameRecordDao {
    @Insert
    suspend fun insert(record: GameRecord): Long

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentGames(limit: Int = 10): List<GameRecord>

    @Query("SELECT * FROM game_records WHERE seriesId = :seriesId ORDER BY timestamp ASC")
    suspend fun getGamesForSeries(seriesId: Long): List<GameRecord>

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastGame(): GameRecord?
}
