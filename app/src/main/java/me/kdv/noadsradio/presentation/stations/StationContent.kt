package me.kdv.noadsradio.presentation.stations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.kdv.noadsradio.domain.model.Station

@Composable
fun StationContent(
    stationState: State<StationState>,
    movedStationGroupId: Int?,
    onStationClickListener: (Station) -> Unit,
    onStationScrollListener: (Station) -> Unit
) {
    when (val currentState = stationState.value) {
        is StationState.Initial -> {}
        is StationState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is StationState.Stations -> {
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
    }
}