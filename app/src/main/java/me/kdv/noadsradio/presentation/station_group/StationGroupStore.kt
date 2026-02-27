package me.kdv.noadsradio.presentation.station_group

import com.arkivanov.mvikotlin.core.store.Store
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup

interface StationGroupStore : Store<StationGroupStore.Intent, StationGroupStore.State, StationGroupStore.Label> {

    data class State(
        val currentStationGroup: StationGroup? = null,
        val stations: List<Station>
    ) {
    }

    sealed interface Label {
        data object OnBackClick : Label
    }

    sealed interface Intent {
        data object OnBackClick: Intent
    }
}

