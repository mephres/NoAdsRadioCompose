package me.kdv.noadsradio.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.data.database.model.StationDb
import me.kdv.noadsradio.domain.model.StationPlaybackState

@Dao
interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationDb>)

    @Query("SELECT * FROM station WHERE enabled = 1")
    fun getStations(): Flow<List<StationDb>>

    @Query("DELETE FROM station")
    suspend fun deleteStations()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStation(station: StationDb)

    @Query("UPDATE station SET state = :state")
    suspend fun resetAllStations(state: Int = StationPlaybackState.STOPPED.ordinal)

    @Query("SELECT * FROM station WHERE id = :id")
    fun getStationById(id: Int): StationDb

    @Query("SELECT * FROM station WHERE url = :url")
    fun getStationById(url: String): LiveData<StationDb>

    @Query("UPDATE station SET state = CASE WHEN id = :id THEN :state WHEN id <> :id THEN 2 END")
    suspend fun setStationStateBy(id: Int, state: Int)
}