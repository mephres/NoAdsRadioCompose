package me.kdv.noadsradio.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.kdv.noadsradio.data.database.dao.StationDao
import me.kdv.noadsradio.data.database.mapper.StationMapper
import me.kdv.noadsradio.data.network.model.StationDto
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.repository.StationRepository
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(
    private val stationMapper: StationMapper,
    private val stationDao: StationDao
) : StationRepository {

    override suspend fun insertStationList(stations: List<StationDto>) {
        val dbList = stations.map {
            stationMapper.mapDtoToDb(it)
        }
        stationDao.insertStations(dbList)
    }

    override suspend fun deleteStations() {
        stationDao.deleteStations()
    }

    override suspend fun updateStation(station: Station) {
        val db = stationMapper.mapEntityToDb(station)
        stationDao.updateStation(db)
    }

    override fun getStations(): Flow<List<Station>> {
        return stationDao.getStations().map {
            it.map {
                stationMapper.mapDbToEntity(it)
            }
        }
    }

    override suspend fun resetAllStations() {
        stationDao.resetAllStations()
    }

    override fun getStationByUrl(url: String): LiveData<Station> {
        return stationDao.getStationById(url).map {
            stationMapper.mapDbToEntity(it)
        }
    }

    override suspend fun setStationState(id: Int, state: Int) {
        stationDao.setStationStateBy(id = id, state = state)
    }
}