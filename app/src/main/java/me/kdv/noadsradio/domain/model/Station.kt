package me.kdv.noadsradio.domain.model

data class Station(
    val id: Int,
    val stationId: Int,
    val groupId: Int,
    val groupName: String,
    val name: String,
    val url: String,
    val noJingle: Boolean,
    val image: String,
    val smallTitle: String,
    var state: StationPlaybackState = StationPlaybackState.STOPPED,
    var position: Int = 0,
    var songTitle: String? = null
)

enum class StationPlaybackState{
    PLAYING,
    LOADED,
    STOPPED
}
