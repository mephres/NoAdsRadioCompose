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

class DefaultMainComponent @AssistedInject constructor(
    private val mainStoreFactory: MainStoreFactory,
    @Assisted("componentContext") componentContext: ComponentContext,
    ): MainComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        mainStoreFactory.create()
    }

    init {
        componentScope().launch {
            store.labels.collect {
                when (it) {

                    else -> {}
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<MainStore.State>
        get() = store.stateFlow

    override fun setIsCurrentStationGroup(id: Int) {
        store.accept(MainStore.Intent.SetIsCurrentStationGroup(id))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultMainComponent
    }
}
