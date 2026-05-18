package `in`.iniyan.ttt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import `in`.iniyan.ttt.data.dao.*
import `in`.iniyan.ttt.data.entity.*

@Database(
    entities = [
        PlayerStats::class,
        Achievement::class,
        DailyChallenge::class,
        UnlockedSkin::class,
        GameRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerStatsDao(): PlayerStatsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
    abstract fun unlockedSkinDao(): UnlockedSkinDao
    abstract fun gameRecordDao(): GameRecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tictactoetrio.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
