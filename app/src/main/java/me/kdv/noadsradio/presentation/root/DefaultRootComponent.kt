package com.intas.monitortsom.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.intas.monitortsom.presentation.auth.DefaultAuthComponent
import com.intas.monitortsom.presentation.main.DefaultMainComponent
import com.intas.monitortsom.presentation.options.DefaultOptionsComponent
import com.intas.monitortsom.presentation.pdf_view.DefaultPdfViewComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val optionsComponentFactory: DefaultOptionsComponent.Factory,
    private val authComponentFactory: DefaultAuthComponent.Factory,
    private val mainComponentFactory: DefaultMainComponent.Factory,
    private val pdfViewComponentFactory: DefaultPdfViewComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = childStack(
            source = navigation,
            initialConfiguration = Config.Auth,
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Auth -> {
                val component = authComponentFactory.create(
                    onOptionsClicked = {
                        navigation.push(Config.Options)
                    },
                    onAuthClicked = {
                        navigation.push(Config.Main)
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Auth(component)
            }
            is Config.Options -> {
                val component = optionsComponentFactory.create(
                    onBackClicked = {
                        navigation.pop()
                    },
                    onOptionsSaved = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Options(component)
            }

            is Config.Main -> {
                val component = mainComponentFactory.create(
                    componentContext = componentContext,
                    onPdfViewOpened = {
                        navigation.push(Config.PdfView(
                            pdfUrl = "https://ncu.rcnpv.com.tw/Uploads/20131231103232738561744.pdf"
                        ))
                    }
                )
                RootComponent.Child.Main(component)
            }

            is Config.PdfView -> {
                val component = pdfViewComponentFactory.create(
                    componentContext = componentContext,
                    pdfUrl = config.pdfUrl,
                    onBackClicked = {
                        navigation.pop()
                    }
                )
                RootComponent.Child.PdfView(component)
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Options : Config

        @Parcelize
        data object Auth : Config

        @Parcelize
        data object Main : Config

        @Parcelize
        data class PdfView(val pdfUrl: String) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultRootComponent
    }
}