package me.kdv.noadsradio.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.kdv.noadsradio.data.database.dao.StationGroupDao
import me.kdv.noadsradio.data.database.mapper.StationGroupMapper
import me.kdv.noadsradio.data.network.FBDataBase
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import me.kdv.noadsradio.domain.repository.StationRepository
import javax.inject.Inject

class StationGroupRepositoryImpl @Inject constructor(
    private val stationGroupMapper: StationGroupMapper,
    private val stationGroupDao: StationGroupDao,
    private val stationRepository: StationRepository
) : StationGroupRepository {

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun loadStationList() {
        FBDataBase.getStationInfo({ stationList ->
            GlobalScope.launch(Dispatchers.IO) {

                val dbList = stationList.map {
                    stationGroupMapper.mapDtoToDb(it)
                }

                stationGroupDao.insertGroups(dbList)
                stationGroupDao.setStationIsCurrentBy(1)
                stationRepository.deleteStations()

                stationList.forEach { stationGroupDto ->
                    stationGroupDto.stations?.let { stations ->
                        val stationsDtoList = stations.map {
                            it.groupId = stationGroupDto.id
                            it.groupName = stationGroupDto.description
                            it
                        }
                        stationRepository.insertStationList(stationsDtoList)
                    }
                }
            }
        }, { error ->

        })
    }

    override fun getStationGroups(): Flow<List<StationGroup>> {
        return stationGroupDao.getGroups().map {
            it.map {
                stationGroupMapper.mapDbToEntity(it)
            }
        }
    }

    override suspend fun updateStationGroups(stationGroups: List<StationGroup>) {
        val db = stationGroups.map {
            stationGroupMapper.mapEntityToDb(it)
        }
        stationGroupDao.insertGroups(db)
    }

    override suspend fun resetStationGroup() {
        stationGroupDao.resetGroups()
    }

    override suspend fun setStationIsCurrentBy(id: Int) {
        stationGroupDao.setStationIsCurrentBy(id = id)
    }
}