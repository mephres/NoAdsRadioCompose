package me.kdv.noadsradio.presentation.station_group

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.kdv.noadsradio.core.componentScope
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.domain.model.StationPlaybackState
import me.kdv.noadsradio.presentation.main.MainStore

class DefaultStationGroupComponent @AssistedInject constructor(
    private val stationGroupStoreFactory: StationGroupStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("stationGroup") private val stationGroup: StationGroup,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit
) : StationGroupComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        stationGroupStoreFactory.create(stationGroup = stationGroup)
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    StationGroupStore.Label.OnBackClick -> {
                        onBackClicked.invoke()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<StationGroupStore.State>
        get() = store.stateFlow

    override fun onBackClick() {
        store.accept(StationGroupStore.Intent.OnBackClick)
    }

    override fun onChangeStationState(station: Station, state: StationPlaybackState) {
        store.accept(
            StationGroupStore.Intent.OnChangeStationState(
                station = station,
                state = state
            )
        )
    }

    override fun onStopPlaying() {
        store.accept(StationGroupStore.Intent.OnStopPlaying)
    }

    override fun onPlayStation(
        station: Station,
        onMediaMetadataChanged: (String) -> Unit,
        onPlaybackStateChanged: (Int) -> Unit
    ) {
        store.accept(
            StationGroupStore.Intent.OnPlayStation(
                station = station,
                onPlaybackStateChanged = { onPlaybackStateChanged(it) },
                onMediaMetadataChanged = { onMediaMetadataChanged(it) }
            )
        )
    }


    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("stationGroup") stationGroup: StationGroup,
            @Assisted("onBackClicked") onBackClicked: () -> Unit
        ): DefaultStationGroupComponent
    }
}
