package me.kdv.noadsradio.presentation.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.usecases.GetStationGroupsUseCase
import me.kdv.noadsradio.domain.usecases.GetStationsUseCase
import me.kdv.noadsradio.domain.usecases.LoadStationsUseCase
import javax.inject.Inject

class MainStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val loadStationsUseCase: LoadStationsUseCase,
    private val getStationGroupsUseCase: GetStationGroupsUseCase,
    private val getStationsUseCase: GetStationsUseCase,

    ) {

    fun create(): MainStore =
        object : MainStore,
            Store<MainStore.Intent, MainStore.State, MainStore.Label> by storeFactory.create(
                name = "MainStore",
                initialState = MainStore.State(
                    field = "",
                    stationGroupState = MainStore.State.StationGroupState.Initial,
                    stationState = MainStore.State.StationState.Initial
                ),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

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
    }

    private inner class BootstrapperImpl() : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                loadStationsUseCase()
                try {
                    dispatch(Action.StationGroupsStartLoading)
                    getStationGroupsUseCase().collect {
                        dispatch(Action.StationGroupsLoaded(stationGroups = it))
                    }
                } catch (e: Exception) {
                    dispatch(Action.StationGroupsLoadingError)
                }
                try {
                    dispatch(Action.StationsStartLoading)
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
                else -> {}
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
            }
        }
    }
}
