package me.kdv.noadsradio.presentation

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Stable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import me.kdv.noadsradio.domain.model.Station
import javax.inject.Inject

@Stable
class MusicPlayer @Inject constructor(private val context: Context) {

    private lateinit var player: Player

    @OptIn(UnstableApi::class)
    fun initMusicPlayer(onCurrentMediaId: (String) -> Unit) {
        val sessionToken =
            SessionToken(context, ComponentName(context, MusicPlayerService::class.java))

        val mediaControllerFuture = MediaController.Builder(context, sessionToken).buildAsync()

        mediaControllerFuture.addListener({
            player = mediaControllerFuture.get()

            player?.currentMediaItem?.mediaId?.let {
                //viewModel.setCurrentMediaId(it)
                onCurrentMediaId(it)
            }
        }, MoreExecutors.directExecutor())
    }

    fun playStation(
        station: Station,
        onMediaMetadataChanged: (String) -> Unit,
        onPlaybackStateChanged: (Int) -> Unit
    ) {

        player?.let { player ->

            val uri = Uri.parse(station.url)

            val newItem = MediaItem.Builder().setMediaId("$uri").build()

            player.setMediaItem(newItem)
            player.prepare()
            player.play()

            player.addListener(object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    super.onMediaMetadataChanged(mediaMetadata)
                    val buffer = mediaMetadata.title.toString().trim()
                        .replace("\n", "")
                        .replace("\r", "")
                        .replace("null", "")
                    onMediaMetadataChanged(buffer)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    onPlaybackStateChanged(playbackState)
                }
            }
            )
        }
    }

    fun stopPlaying() {
        player?.let { player ->
            player.playWhenReady = false
            player.stop()
        }
    }
}