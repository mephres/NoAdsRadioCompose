package me.kdv.noadsradio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import dagger.hilt.android.AndroidEntryPoint
import me.kdv.noadsradio.presentation.root.DefaultRootComponent
import me.kdv.noadsradio.ui.content.RootContent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /*private val viewModel by viewModels<MainViewModel>()

    private var currentStation: Station? = null*/

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //viewModel.loadStationList()

        enableEdgeToEdge()
        setContent {

            enableEdgeToEdge()
            setContent {
                RootContent(component = rootComponentFactory.create(defaultComponentContext()))
            }
            /*
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
            }*/
        }
    }
}
