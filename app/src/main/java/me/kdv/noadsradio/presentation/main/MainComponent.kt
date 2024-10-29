package me.kdv.noadsradio.presentation.main

import kotlinx.coroutines.flow.StateFlow
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState

interface MainComponent {
    val model: StateFlow<MainStore.State>

    fun setIsCurrentStationGroup(id: Int)
    fun changeMovedStationGroupId(id: Int)
    fun changeStationState(station: Station, state: StationPlaybackState)
    fun stopPlaying()
    fun playStation(
        station: Station,
        onMediaMetadataChanged: (String) -> Unit,
        onPlaybackStateChanged: (Int) -> Unit
    )
}