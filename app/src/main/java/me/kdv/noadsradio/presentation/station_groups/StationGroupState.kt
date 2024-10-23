package me.kdv.noadsradio.presentation.station_groups

import me.kdv.noadsradio.domain.model.StationGroup

sealed class StationGroupState {
    object Initial: StationGroupState()
    object Loading: StationGroupState()
    data class StationGroups(val stationGroups: List<StationGroup>): StationGroupState()
}