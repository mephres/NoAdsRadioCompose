package me.kdv.noadsradio.presentation.station_group

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.usecases.GetStationsByGroupIdUseCase
import me.kdv.noadsradio.domain.usecases.SetStationStateUseCase
import me.kdv.noadsradio.presentation.MusicPlayer
import javax.inject.Inject

class StationGroupStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val musicPlayer: MusicPlayer,
    private val getStationsByGroupIdUseCase: GetStationsByGroupIdUseCase,
    private val setStationStateUseCase: SetStationStateUseCase,
) {

    fun create(stationGroup: StationGroup): StationGroupStore =
        object : StationGroupStore,
            Store<StationGroupStore.Intent, StationGroupStore.State, StationGroupStore.Label> by storeFactory.create(
                name = "StationGroupStore",
                initialState = StationGroupStore.State(
                    currentStationGroup = stationGroup,
                    stations = listOf()
                ),
                bootstrapper = BootstrapperImpl(stationGroupId = stationGroup.id),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {
        }

    private sealed interface Action {
        data class StationsLoaded(val stations: List<Station>) : Action
    }

    private sealed interface Msg {
        data class StationsLoaded(val stations: List<Station>) : Msg
    }

    private inner class BootstrapperImpl(val stationGroupId: Int) :
        CoroutineBootstrapper<Action>() {
        override fun invoke() {
            musicPlayer.initMusicPlayer {
                val a = 1
            }
            scope.launch {
                getStationsByGroupIdUseCase(groupId = stationGroupId).collect {
                    dispatch(Action.StationsLoaded(stations = it))
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<StationGroupStore.Intent, Action, StationGroupStore.State, Msg, StationGroupStore.Label>() {
        override fun executeIntent(
            intent: StationGroupStore.Intent,
            getState: () -> StationGroupStore.State
        ) {
            when (intent) {
                StationGroupStore.Intent.OnBackClick -> {
                    publish(StationGroupStore.Label.OnBackClick)
                }

                is StationGroupStore.Intent.OnPlayStation -> {
                    musicPlayer.playStation(
                        station = intent.station,
                        onMediaMetadataChanged = {
                            intent.onMediaMetadataChanged(it)
                        }, onPlaybackStateChanged = {
                            intent.onPlaybackStateChanged(it)
                        }
                    )
                }

                StationGroupStore.Intent.OnStopPlaying -> musicPlayer.stopPlaying()
                is StationGroupStore.Intent.OnChangeStationState -> {
                    scope.launch {
                        setStationStateUseCase(id = intent.station.id, state = intent.state.ordinal)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> StationGroupStore.State) {
            when (action) {
                is Action.StationsLoaded -> {
                    dispatch(Msg.StationsLoaded(stations = action.stations))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<StationGroupStore.State, Msg> {
        override fun StationGroupStore.State.reduce(msg: Msg): StationGroupStore.State {
            return when (msg) {
                is Msg.StationsLoaded -> {
                    this.copy(stations = msg.stations)
                }
            }
        }
    }
}
