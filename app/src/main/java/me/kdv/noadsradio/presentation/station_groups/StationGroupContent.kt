package me.kdv.noadsradio.presentation.station_groups

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.presentation.MainViewModel

@Composable
fun StationGroupContent(
    stationGroupState: State<StationGroupState>,
            viewModel: MainViewModel,
    onStationGroupClickListener: (StationGroup) -> Unit
) {
    when (val currentState = stationGroupState.value) {
        is StationGroupState.StationGroups -> {
            StationGroups(
                stationGroupList = currentState.stationGroups) {
                viewModel.setIsCurrentStationGroup(it.id)
                onStationGroupClickListener.invoke(it)
            }
        }
        is StationGroupState.Initial -> {}
        is StationGroupState.Loading -> {
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