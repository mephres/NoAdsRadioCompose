package me.kdv.noadsradio.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.domain.repository.StationGroupRepository
import me.kdv.noadsradio.domain.repository.StationRepository
import me.kdv.noadsradio.presentation.station_groups.StationGroupState
import me.kdv.noadsradio.presentation.stations.StationPlayingState
import me.kdv.noadsradio.presentation.stations.StationState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stationGroupRepository: StationGroupRepository,
    private val stationRepository: StationRepository,
) : ViewModel() {


    val stationGroupState = stationGroupRepository.getStationGroups()
        .filter {
            it.isNotEmpty()
        }.map {
            StationGroupState.StationGroups(stationGroups = it) as StationGroupState
        }.onStart {
            StationGroupState.Loading
        }

    val stations = stationRepository.getStations()
        .filter {
            it.isNotEmpty()
        }.map {
            StationState.Stations(stations = it) as StationState
        }.onStart {
            StationState.Loading
        }

    private var currentStationGroup: StationGroup? = null

    private var _currentMediaId: MutableLiveData<String> = MutableLiveData()
    val currentMediaId: LiveData<String>
        get() = _currentMediaId

    fun setCurrentMediaId(mediaId: String) {
        _currentMediaId.value = mediaId
    }

    fun getCurrentPlayingStation(url: String): LiveData<Station> {
        return stationRepository.getStationByUrl(url)
    }

    fun loadStationList() {
        viewModelScope.launch {
            stationGroupRepository.loadStationList()
        }
    }

    fun changeStationState(station: Station, state: StationPlaybackState) {
        viewModelScope.launch {
            station.state = state
            stationRepository.updateStation(station)
        }
    }

    fun setIsCurrentStationGroup(stationGroupId: Int) {

        viewModelScope.launch {
            stationGroupRepository.setStationIsCurrentBy(id = stationGroupId)
        }
    }

    private fun resetAllStations() {
        viewModelScope.launch {
            stationRepository.resetAllStations()
        }
    }
}