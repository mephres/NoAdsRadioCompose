package me.kdv.noadsradio

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.presentation.MainViewModel
import me.kdv.noadsradio.presentation.station_groups.StationGroupContent
import me.kdv.noadsradio.presentation.station_groups.StationGroupState
import me.kdv.noadsradio.presentation.station_info.StationInfo
import me.kdv.noadsradio.presentation.stations.StationContent
import me.kdv.noadsradio.presentation.stations.StationState
import me.kdv.noadsradio.ui.theme.NoAdsRadioTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private var currentStation: Station? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadStationList()

        enableEdgeToEdge()
        setContent {

            val stationGroupState =
                viewModel.stationGroupState.collectAsState(initial = StationGroupState.Initial)

            val stationState = viewModel.stations.collectAsState(initial = StationState.Initial)

            var movedStationGroupId by rememberSaveable {
                mutableIntStateOf(-1)
            }

            var isVisibleStationInfo by rememberSaveable {
                mutableStateOf(false)
            }

            NoAdsRadioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        Spacer(modifier = Modifier.height(0.dp))

                        Box {
                            StationGroupContent(
                                stationGroupState = stationGroupState,
                                viewModel = viewModel
                            ) {
                                movedStationGroupId = it.id
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(modifier = Modifier.weight(1f)) {
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
                        }
                    }
                }
            }
        }
    }



}
