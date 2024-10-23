package me.kdv.noadsradio.presentation.main

import kotlinx.coroutines.flow.StateFlow

interface MainComponent {
    val model: StateFlow<MainStore.State>

    fun setIsCurrentStationGroup(id: Int)
}