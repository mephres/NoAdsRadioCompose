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
import me.kdv.noadsradio.presentation.root.DefaultRootComponent
import me.kdv.noadsradio.presentation.root.RootComponent
import me.kdv.noadsradio.ui.theme.NoAdsRadioTheme

@Composable
fun RootContent(
    component: DefaultRootComponent
) {
    NoAdsRadioTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(stack = component.stack) {
                val currentInstance by remember {
                    mutableStateOf(it.instance)
                }
                AnimatedContent(targetState = currentInstance, label = "RootAnimatedContent") { instance->
                    when(instance) {
                        is RootComponent.Child.Main -> {
                            MainScreen(component = instance.component)
                        }
                    }
                }
            }
        }
    }
}