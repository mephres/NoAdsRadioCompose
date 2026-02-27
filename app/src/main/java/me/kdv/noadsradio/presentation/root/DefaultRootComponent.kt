package me.kdv.noadsradio.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import me.kdv.noadsradio.domain.model.StationGroup
import me.kdv.noadsradio.presentation.main.DefaultMainComponent
import me.kdv.noadsradio.presentation.station_group.DefaultStationGroupComponent

class DefaultRootComponent @AssistedInject constructor(
    private val mainComponentFactory: DefaultMainComponent.Factory,
    private val stationGroupComponentFactory: DefaultStationGroupComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Main,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {

            is Config.Main -> {
                val component = mainComponentFactory.create(
                    componentContext = componentContext,
                    onOpenStationGroup = { stationGroup ->
                        navigation.push(Config.StationGroup(stationGroup = stationGroup))
                    }
                )
                RootComponent.Child.Main(component)
            }

            is Config.StationGroup -> {
                val component = stationGroupComponentFactory.create(
                    componentContext = componentContext,
                    stationGroup = config.stationGroup,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.StationGroup(component)
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Main : Config

        @Parcelize
        data class StationGroup(val stationGroup: me.kdv.noadsradio.domain.model.StationGroup) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}