package `in`.iniyan.tictactoetrio.data.dao

import androidx.room.*
import `in`.iniyan.tictactoetrio.data.entity.DailyChallenge
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    suspend fun getChallengeForDate(date: String): DailyChallenge?

    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    fun observeChallengeForDate(date: String): Flow<DailyChallenge?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(challenge: DailyChallenge)

    @Query("UPDATE daily_challenges SET isCompleted = 1 WHERE date = :date")
    suspend fun markCompleted(date: String)
}
