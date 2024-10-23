package me.kdv.noadsradio.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kdv.noadsradio.presentation.main.MainComponent
import me.kdv.noadsradio.ui.content.station_group.StationGroupContent

@Composable
fun MainScreen(component: MainComponent) {
    val model by component.model.collectAsState()

    var movedStationGroupId by rememberSaveable {
        mutableIntStateOf(-1)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(0.dp))

            Box {
                StationGroupContent(
                    stationGroupState = model.stationGroupState,
                ) {
                    component.setIsCurrentStationGroup(it.id)
                    movedStationGroupId = it.id
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*Box(modifier = Modifier.weight(1f)) {
                StationContent(stationState = stationState,
                    movedStationGroupId = movedStationGroupId,
                    onStationClickListener = {
                        currentStation = it
                        isVisibleStationInfo = !isVisibleStationInfo
                        viewModel.changeStationState(it, StationPlaybackState.LOADED)
                    },
                    onStationScrollListener = { station ->
                        val stationGroupId = station.groupId
                        viewModel.setIsCurrentStationGroup(stationGroupId)
                        Log.d("MainActivity->onStationScrollListener", "$station")
                        movedStationGroupId = -1
                    })
            }

            Box {
                if (isVisibleStationInfo) {
                    currentStation?.let {
                        StationInfo(station = it, songTitle = "Test Song") {

                        }
                    }

                }
            }*/
        }
    }
}