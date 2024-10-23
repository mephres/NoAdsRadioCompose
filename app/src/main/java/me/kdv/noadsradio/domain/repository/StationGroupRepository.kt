package me.kdv.noadsradio.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import me.kdv.noadsradio.domain.model.StationGroup

interface StationGroupRepository {
    suspend fun loadStationList()
    fun getStationGroups(): Flow<List<StationGroup>>
    suspend fun updateStationGroups(stationGroups: List<StationGroup>)
    suspend fun resetStationGroup()
    suspend fun setStationIsCurrentBy(id: Int)
}