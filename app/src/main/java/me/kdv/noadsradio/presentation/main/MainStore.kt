package me.kdv.noadsradio.presentation.main

import com.arkivanov.mvikotlin.core.store.Store
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup

interface MainStore : Store<MainStore.Intent, MainStore.State, MainStore.Label> {

    data class State(
        val field: String,
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
        /*data object ClickHome : Label
        data object ClickPdfView : Label*/
    }

    sealed interface Intent {
        data class SetIsCurrentStationGroup(val id: Int): Intent
        /*data class ChangeEmailOnHome(val email: String) : Intent
        data object ClickBack : Intent
        data object ClickPdfView : Intent
        data class ChangeNavigationRoute(val route: String) : Intent*/
    }
}

