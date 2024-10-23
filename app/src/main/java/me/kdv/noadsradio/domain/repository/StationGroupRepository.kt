package me.kdv.noadsradio.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.kdv.noadsradio.domain.model.StationGroup

interface StationGroupRepository {
    suspend fun loadStationList()
    fun getStationGroups(): LiveData<List<StationGroup>>
    suspend fun updateStationGroups(stationGroups: List<StationGroup>)
    suspend fun resetStationGroup()
    suspend fun setStationIsCurrentBy(id: Int)
}