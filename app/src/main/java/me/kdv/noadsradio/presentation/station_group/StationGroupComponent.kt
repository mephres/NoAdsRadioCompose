package me.kdv.noadsradio.presentation.station_group

import kotlinx.coroutines.flow.StateFlow
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState

interface StationGroupComponent {
    val model: StateFlow<StationGroupStore.State>

    fun onBackClick()
}