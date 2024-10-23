package me.kdv.noadsradio.ui.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.intas.monitortsom.presentation.root.DefaultRootComponent
import com.intas.monitortsom.presentation.root.RootComponent
import com.intas.monitortsom.ui.content.main.PdfViewScreen
import com.intas.monitortsom.ui.theme.MonitorTsomTheme

@Composable
fun RootContent(
    component: DefaultRootComponent,
    onLocaleChanged: (String) -> Unit
) {
    MonitorTsomTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(stack = component.stack) {
                val currentInstance by remember {
                    mutableStateOf(it.instance)
                }
                AnimatedContent(targetState = currentInstance, label = "RootAnimatedContent") { instance->
                    when(instance) {
                        is RootComponent.Child.Auth -> {
                            LoginScreen(component = instance.component)
                        }
                        is RootComponent.Child.Options -> {
                            OptionsScreen(component = instance.component) {
                                onLocaleChanged(it)
                            }
                        }
                        is RootComponent.Child.Main -> {
                            MainScreen(component = instance.component)
                        }

                        is RootComponent.Child.PdfView -> {
                            PdfViewScreen(component = instance.component)
                        }
                    }
                }
            }
        }
    }
}