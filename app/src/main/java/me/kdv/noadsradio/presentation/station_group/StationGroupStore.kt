package me.kdv.noadsradio.presentation.station_group

import com.arkivanov.mvikotlin.core.store.Store
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.presentation.main.MainStore.Intent
import me.kdv.noadsradio.presentation.main.MainStore.State.StationState

interface StationGroupStore : Store<StationGroupStore.Intent, StationGroupStore.State, StationGroupStore.Label> {

    data class State(
        val currentStationGroup: StationGroup? = null,
        val stations: List<Station>,
    ) {
    }

    sealed interface Label {
        data object OnBackClick : Label
    }

    sealed interface Intent {
        data object OnBackClick: Intent

        data class OnChangeStationState(val station: Station, val state: StationPlaybackState): Intent
        data object OnStopPlaying : Intent
        data class OnPlayStation(
            val station: Station,
            val onMediaMetadataChanged: (String) -> Unit,
            val onPlaybackStateChanged: (Int) -> Unit
        ) : Intent
    }
}

