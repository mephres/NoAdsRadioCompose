package me.kdv.noadsradio.presentation.stations

sealed class StationPlayingState {
    object Initial: StationPlayingState()
    object Loading: StationPlayingState()
    object Playing: StationPlayingState()
    object Stopped: StationPlayingState()
}