package `in`.iniyan.ttt.data.dao

import androidx.room.*
import `in`.iniyan.ttt.data.entity.UnlockedSkin
import kotlinx.coroutines.flow.Flow

@Dao
interface UnlockedSkinDao {
    @Query("SELECT * FROM unlocked_skins")
    fun getAllSkins(): Flow<List<UnlockedSkin>>

    @Query("SELECT * FROM unlocked_skins WHERE type = :type AND isActive = 1 LIMIT 1")
    suspend fun getActiveSkin(type: String): UnlockedSkin?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(skin: UnlockedSkin)

    @Query("UPDATE unlocked_skins SET isActive = 0 WHERE type = :type")
    suspend fun deactivateAllOfType(type: String)

    @Query("UPDATE unlocked_skins SET isActive = 1 WHERE skinId = :skinId")
    suspend fun activate(skinId: String)
}
