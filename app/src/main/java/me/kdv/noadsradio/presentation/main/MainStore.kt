package me.kdv.noadsradio.presentation.main

import com.arkivanov.mvikotlin.core.store.Store
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.model.StationPlaybackState

interface MainStore : Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    data class State(
        val currentStation: Station? = null,
        val movedStationGroupId: Int = -1,
        val stationGroupState: StationGroupState,
        val stationState: StationState
    ) {
        sealed interface StationGroupState {
            data object Initial : StationGroupState
            data object Loading : StationGroupState
            data object Error : StationGroupState
            data class Loaded(val stationGroups: List<StationGroup>) : StationGroupState
        }

        sealed interface StationState {
            data object Initial : StationState
            data object Loading : StationState
            data object Error : StationState
            data class Loaded(val stations: List<Station>) : StationState
        }
    }

    sealed interface Label {
        data class OnOpenStationGroup(val stationGroup: StationGroup): Label
    }

    sealed interface Intent {
        data class OnOpenStationGroup(val stationGroup: StationGroup) : Intent
        data class SetIsCurrentStationGroup(val id: Int) : Intent
        data class ChangeMovedStationGroupId(val id: Int) : Intent
        data class ChangeStationState(val station: Station, val state: StationPlaybackState) :
            Intent

        data object StopPlaying : Intent
        data class PlayStation(
            val station: Station,
            val onMediaMetadataChanged: (String) -> Unit,
            val onPlaybackStateChanged: (Int) -> Unit
        ) : Intent
    }
}

