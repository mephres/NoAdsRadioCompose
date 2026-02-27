package me.kdv.noadsradio.presentation.main

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

class DefaultMainComponent @AssistedInject constructor(
    private val mainStoreFactory: MainStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    @Assisted("onOpenStationGroup") private val onOpenStationGroup: (StationGroup) -> Unit,
) : MainComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        mainStoreFactory.create()
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {
                    is MainStore.Label.OnOpenStationGroup -> {
                        onOpenStationGroup.invoke(it.stationGroup)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MainStore.State>
        get() = store.stateFlow

    override fun onOpenStationGroup(stationGroup: StationGroup) {
        store.accept(MainStore.Intent.OnOpenStationGroup(stationGroup = stationGroup))
    }

    override fun setIsCurrentStationGroup(id: Int) {
        store.accept(MainStore.Intent.SetIsCurrentStationGroup(id))
    }

    override fun changeMovedStationGroupId(id: Int) {
        store.accept(MainStore.Intent.ChangeMovedStationGroupId(id))
    }

    override fun changeStationState(station: Station, state: StationPlaybackState) {
        store.accept(MainStore.Intent.ChangeStationState(station = station, state = state))
    }

    override fun stopPlaying() {
        store.accept(MainStore.Intent.StopPlaying)
    }

    override fun playStation(
        station: Station,
        onMediaMetadataChanged: (String) -> Unit,
        onPlaybackStateChanged: (Int) -> Unit
    ) {
        store.accept(
            MainStore.Intent.PlayStation(
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
            @Assisted("onOpenStationGroup") onOpenStationGroup: (StationGroup) -> Unit,
        ): DefaultMainComponent
    }
}
