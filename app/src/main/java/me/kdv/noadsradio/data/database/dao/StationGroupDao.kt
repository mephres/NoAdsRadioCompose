package me.kdv.noadsradio.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.data.database.model.StationGroupDb

@Dao
interface StationGroupDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroups(groups: List<StationGroupDb>)

    @Query("SELECT * FROM station_group")
    fun getGroups(): Flow<List<StationGroupDb>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGroups(groups: List<StationGroupDb>)

    @Query("UPDATE station_group SET isCurrent = 0")
    suspend fun resetGroups()

    @Query("UPDATE station_group SET isCurrent = CASE 1 WHEN id = :id THEN 1 WHEN id <> :id THEN 0 END")
    suspend fun setStationIsCurrentBy(id: Int)
}