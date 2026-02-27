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
import me.kdv.noadsradio.domain.model.StationGroup

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


    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("stationGroup") stationGroup: StationGroup,
            @Assisted("onBackClicked") onBackClicked: () -> Unit
        ): DefaultStationGroupComponent
    }
}
