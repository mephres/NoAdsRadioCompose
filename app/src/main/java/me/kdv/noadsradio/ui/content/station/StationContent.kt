package me.kdv.noadsradio.ui.content.station

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.presentation.main.MainStore

@Composable
fun StationContent(
    stationState: MainStore.State.StationState,
    movedStationGroupId: Int,
    onStationClickListener: (Station) -> Unit,
    onStationScrollListener: (Station) -> Unit
) {
    when (val currentState = stationState) {
        MainStore.State.StationState.Error -> {}
        MainStore.State.StationState.Initial -> {}
        is MainStore.State.StationState.Loaded -> {
            Stations(
                stationList = currentState.stations,
                movedStationGroupId = movedStationGroupId,
                onStationClickListener = {
                    onStationClickListener.invoke(it)
                },
                onStationScrollListener = { station ->
                    onStationScrollListener.invoke(station)
                }
            )
        }
        MainStore.State.StationState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}