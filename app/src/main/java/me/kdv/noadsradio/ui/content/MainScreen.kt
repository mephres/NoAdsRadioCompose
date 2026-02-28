package me.kdv.noadsradio.ui.content

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.presentation.MusicPlayer
import me.kdv.noadsradio.presentation.main.MainComponent
import me.kdv.noadsradio.ui.content.station.StationContent
import me.kdv.noadsradio.ui.content.station_group.StationGroupContent
import me.kdv.noadsradio.ui.content.station_info.StationInfo

@Composable
fun MainScreen(component: MainComponent) {
    val model by component.model.collectAsState()

    var currentStation: Station? by remember {
        mutableStateOf(null)
    }

    var stationInfoIsVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)) {
            Text(text = "Жанры")

            Spacer(modifier = Modifier.height(8.dp))

            Box {
                StationGroupContent(
                    stationGroupState = model.stationGroupState,
                    onStationGroupSelect = {
                        component.onOpenStationGroup(it)
                        component.setIsCurrentStationGroup(it.id)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            /*Box(modifier = Modifier.weight(1f)) {
                StationContent(
                    stationState = model.stationState,
                    movedStationGroupId = model.movedStationGroupId,
                    onStationClickListener = { station ->
                        currentStation = station
                        when (station.state) {
                            StationPlaybackState.PLAYING -> {
                                currentStation?.let {
                                    component.changeStationState(
                                        it,
                                        StationPlaybackState.STOPPED
                                    )
                                    currentStation = null
                                    component.stopPlaying()
                                    stationInfoIsVisible = false
                                }

                            }

                            StationPlaybackState.LOADED -> {
                            }

                            StationPlaybackState.STOPPED -> {

                                currentStation = station

                                component.changeStationState(
                                    station = station,
                                    state = StationPlaybackState.LOADED
                                )

                                component.playStation(
                                    station = station,
                                    onMediaMetadataChanged = { songTitle ->
                                        currentStation?.let { cs ->
                                            currentStation = cs.copy(songTitle = songTitle)
                                        }

                                        stationInfoIsVisible = true
                                    }, onPlaybackStateChanged = { state ->
                                        when (state) {
                                            1 -> {
                                                Log.d("NoAdsPlayerPlaybackState", "STATE_IDLE")
                                                component.stopPlaying()

                                                stationInfoIsVisible = false

                                                currentStation?.let {
                                                    component.changeStationState(
                                                        it,
                                                        StationPlaybackState.STOPPED
                                                    )
                                                    currentStation = null
                                                }
                                            }

                                            2 -> {
                                                Log.d("NoAdsPlayerPlaybackState", "STATE_BUFFERING")
                                                currentStation?.let { currentStation ->
                                                    component.changeStationState(
                                                        currentStation,
                                                        StationPlaybackState.LOADED
                                                    )
                                                }
                                            }

                                            3 -> {
                                                Log.d("NoAdsPlayerPlaybackState", "STATE_READY")
                                                currentStation?.let { currentStation ->
                                                    component.changeStationState(
                                                        currentStation,
                                                        StationPlaybackState.PLAYING
                                                    )
                                                }
                                            }

                                            4 -> {
                                                Log.d("NoAdsPlayerPlaybackState", "STATE_ENDED")
                                            }
                                        }
                                    })
                            }

                        }
                    },
                    onStationScrollListener = { station ->
                        val stationGroupId = station.groupId
                        component.setIsCurrentStationGroup(stationGroupId)
                        Log.d("MainScreen->onStationScrollListener", "$station")
                        component.changeMovedStationGroupId(-1)
                    })
            }*/

            if (stationInfoIsVisible)
                currentStation?.let {
                    Box {
                        StationInfo(station = it) {
                            component.stopPlaying()
                            currentStation = null
                            stationInfoIsVisible = false
                        }
                    }

                }
        }
    }
}