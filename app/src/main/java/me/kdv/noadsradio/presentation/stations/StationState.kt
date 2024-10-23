package me.kdv.noadsradio.presentation.stations

import me.kdv.noadsradio.domain.model.Station

sealed class StationState {
    object Initial: StationState()
    object Loading: StationState()
    data class Stations(val stations: List<Station>): StationState()
}