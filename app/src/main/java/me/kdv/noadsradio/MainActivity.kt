package me.kdv.noadsradio

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import dagger.hilt.android.AndroidEntryPoint
import me.kdv.noadsradio.presentation.MusicPlayer
import me.kdv.noadsradio.presentation.root.DefaultRootComponent
import me.kdv.noadsradio.ui.content.RootContent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val component = rootComponentFactory.create(defaultComponentContext())
        setContent {
            RootContent(component = component)
        }
    }
}
