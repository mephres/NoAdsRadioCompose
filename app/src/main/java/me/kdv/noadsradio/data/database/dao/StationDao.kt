package me.kdv.noadsradio.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.data.database.model.StationDbModel
import me.kdv.noadsradio.domain.model.StationPlaybackState

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationDbModel>)

    @Query("SELECT * FROM station WHERE enabled = 1")
    fun getStations(): Flow<List<StationDbModel>>

    @Query("DELETE FROM station")
    suspend fun deleteStations()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStation(station: StationDbModel)

    @Query("UPDATE station SET state = :state")
    suspend fun resetAllStations(state: Int = StationPlaybackState.STOPPED.ordinal)

    @Query("SELECT * FROM station WHERE id = :id")
    fun getStationById(id: Int): StationDbModel

    @Query("SELECT * FROM station WHERE url = :url")
    fun getStationById(url: String): LiveData<StationDbModel>

    @Query("UPDATE station SET state = CASE WHEN id = :id THEN :state WHEN id <> :id THEN 2 END")
    suspend fun setStationStateBy(id: Int, state: Int)

    @Query("SELECT * FROM station WHERE groupId = :groupId AND enabled = 1")
    fun getStationsByGroupId(groupId: Int): Flow<List<StationDbModel>>
}