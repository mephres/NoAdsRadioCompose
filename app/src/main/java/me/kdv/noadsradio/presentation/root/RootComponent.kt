package me.kdv.noadsradio.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import me.kdv.noadsradio.presentation.main.MainComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Main(val component: MainComponent) : Child
    }
}