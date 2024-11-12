package me.kdv.noadsradio.presentation.main

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.domain.usecases.GetStationGroupsUseCase
import me.kdv.noadsradio.domain.usecases.GetStationsUseCase
import me.kdv.noadsradio.domain.usecases.LoadStationsUseCase
import me.kdv.noadsradio.domain.usecases.SetIsCurrentStationGroupUseCase
import me.kdv.noadsradio.domain.usecases.SetStationStateUseCase
import me.kdv.noadsradio.presentation.MusicPlayer
import me.kdv.noadsradio.presentation.main.MainStore.Intent
import javax.inject.Inject

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loadStationsUseCase: LoadStationsUseCase,
    private val getStationGroupsUseCase: GetStationGroupsUseCase,
    private val getStationsUseCase: GetStationsUseCase,
    private val setIsCurrentStationGroupUseCase: SetIsCurrentStationGroupUseCase,
    private val setStationStateUseCase: SetStationStateUseCase,
    private val musicPlayer: MusicPlayer
    ) {

    fun create(): MainStore =
        object : MainStore,
            Store<MainStore.Intent, MainStore.State, MainStore.Label> by storeFactory.create(
                name = "MainStore",
                initialState = MainStore.State(
                    currentStation = null,
                    stationGroupState = MainStore.State.StationGroupState.Initial,
                    stationState = MainStore.State.StationState.Initial
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {
            }

    private sealed interface Action {
        data object StationGroupsStartLoading : Action
        data object StationGroupsLoadingError : Action
        data class StationGroupsLoaded(val stationGroups: List<StationGroup>) : Action

        data object StationsStartLoading : Action
        data object StationsLoadingError : Action
        data class StationsLoaded(val stations: List<Station>) : Action
    }

    private sealed interface Msg {
        data object StationGroupsStartLoading : Msg
        data object StationGroupsLoadingError : Msg
        data class StationGroupsLoaded(val stationGroups: List<StationGroup>) : Msg

        data object StationsStartLoading : Msg
        data object StationsLoadingError : Msg
        data class StationsLoaded(val stations: List<Station>) : Msg

        data class ChangeMovedStationGroupId(val id: Int) : Msg
    }

    private inner class BootstrapperImpl() : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            musicPlayer.initMusicPlayer {
                val a = 1
            }
            dispatch(Action.StationGroupsStartLoading)
            dispatch(Action.StationsStartLoading)
            scope.launch {
                loadStationsUseCase()
            }
            scope.launch {
                try {
                    getStationGroupsUseCase().collect {
                        dispatch(Action.StationGroupsLoaded(stationGroups = it))
                    }
                } catch (e: Exception) {
                    dispatch(Action.StationGroupsLoadingError)
                }
            }
            scope.launch {
                try {
                    getStationsUseCase().collect {
                        dispatch(Action.StationsLoaded(stations = it))
                    }
                } catch (e: Exception) {
                    dispatch(Action.StationsLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<MainStore.Intent, Action, MainStore.State, Msg, MainStore.Label>() {
        override fun executeIntent(
            intent: MainStore.Intent,
            getState: () -> MainStore.State
        ) {
            when (intent) {
                /*is MainStore.Intent.ClickBack -> {
                    publish(MainStore.Label.ClickHome)
                }

                is MainStore.Intent.ChangeEmailOnHome -> {
                    dispatch(Msg.ChangeEmailOnHome(email = intent.email))
                }

                MainStore.Intent.ClickPdfView -> {
                    publish(MainStore.Label.ClickPdfView)
                }

                is MainStore.Intent.ChangeNavigationRoute -> {
                    dispatch(Msg.ChangeNavigationRoute(route = intent.route))
                }*/
                is Intent.SetIsCurrentStationGroup -> {
                    scope.launch {
                        setIsCurrentStationGroupUseCase(id = intent.id)
                    }
                }

                is Intent.ChangeMovedStationGroupId -> {
                    dispatch(Msg.ChangeMovedStationGroupId(id = intent.id))
                }

                is Intent.ChangeStationState -> {
                    scope.launch {
                        setStationStateUseCase(id = intent.station.id, state = intent.state.ordinal)
                    }
                }

                Intent.StopPlaying -> {
                    musicPlayer.stopPlaying()
                }

                is Intent.PlayStation -> {
                    musicPlayer.playStation(station = intent.station, onMediaMetadataChanged = {
                        intent.onMediaMetadataChanged(it)
                    }, onPlaybackStateChanged = {
                        intent.onPlaybackStateChanged(it)
                    })
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> MainStore.State) {
            when (action) {

                is Action.StationGroupsLoaded -> {
                    dispatch(Msg.StationGroupsLoaded(stationGroups = action.stationGroups))
                }

                is Action.StationsLoaded -> {
                    dispatch(Msg.StationsLoaded(stations = action.stations))
                }

                Action.StationGroupsLoadingError -> {
                    dispatch(Msg.StationGroupsLoadingError)
                }

                Action.StationGroupsStartLoading -> {
                    dispatch(Msg.StationGroupsStartLoading)
                }

                Action.StationsLoadingError -> {
                    dispatch(Msg.StationsLoadingError)
                }

                Action.StationsStartLoading -> {
                    dispatch(Msg.StationsStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<MainStore.State, Msg> {
        override fun MainStore.State.reduce(msg: Msg): MainStore.State {
            return when (msg) {

                is Msg.StationGroupsLoaded -> {
                    copy(stationGroupState = MainStore.State.StationGroupState.Loaded(msg.stationGroups))
                }

                Msg.StationGroupsLoadingError -> {
                    copy(stationGroupState = MainStore.State.StationGroupState.Error)
                }

                Msg.StationGroupsStartLoading -> {
                    copy(stationGroupState = MainStore.State.StationGroupState.Loading)
                }

                is Msg.StationsLoaded -> {
                    copy(stationState = MainStore.State.StationState.Loaded(msg.stations))
                }

                Msg.StationsLoadingError -> {
                    copy(stationState = MainStore.State.StationState.Error)
                }

                Msg.StationsStartLoading -> {
                    copy(stationState = MainStore.State.StationState.Loading)
                }

                is Msg.ChangeMovedStationGroupId -> {
                    copy(movedStationGroupId = msg.id)
                }
            }
        }
    }
}
