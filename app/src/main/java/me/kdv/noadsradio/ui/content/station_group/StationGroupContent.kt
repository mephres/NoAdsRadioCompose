package me.kdv.noadsradio.ui.content.station_group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.presentation.main.MainStore

@Composable
fun StationGroupContent(
    stationGroupState: MainStore.State.StationGroupState,
    onStationGroupSelect: (StationGroup) -> Unit
) {
    when (val currentState = stationGroupState) {
        MainStore.State.StationGroupState.Error -> {

        }
        MainStore.State.StationGroupState.Initial -> {}
        is MainStore.State.StationGroupState.Loaded -> {
            StationGroups(
                stationGroupList = currentState.stationGroups) {
                onStationGroupSelect.invoke(it)
            }
        }
        MainStore.State.StationGroupState.Loading -> {
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