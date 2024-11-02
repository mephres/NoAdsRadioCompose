package me.kdv.noadsradio.ui.content.station_info

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.LottieDynamicProperty
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.model.KeyPath
import me.kdv.noadsradio.R
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState
import kotlin.math.roundToInt

@Composable
fun StationInfo(
    station: Station,
    onStopButtonClicked: (Station) -> (Unit)
) {
    Card(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 0.dp, start = 8.dp, end = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (
                stationTitleText, songTitleText,
                stationControlButton, musicPlayingAnimation
            ) = createRefs()

            Text(
                modifier = Modifier.constrainAs(stationTitleText) {
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(parent.top, margin = 8.dp)
                    end.linkTo(stationControlButton.start)
                    width = Dimension.fillToConstraints
                },
                text = station.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                modifier = Modifier.constrainAs(songTitleText) {
                    start.linkTo(parent.start, margin = 0.dp)
                    top.linkTo(stationTitleText.bottom, margin = 4.dp)
                    bottom.linkTo(musicPlayingAnimation.top, margin = 4.dp)
                    end.linkTo(stationControlButton.start)
                    width = Dimension.matchParent
                },
                text = station.songTitle ?: "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(
                modifier = Modifier
                    .constrainAs(stationControlButton) {
                        end.linkTo(parent.end, margin = 0.dp)
                        top.linkTo(parent.top, margin = 4.dp)
                    }
                    .padding(0.dp)
                    .size(42.dp),
                onClick = {
                    onStopButtonClicked(station)
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_stop_48),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
            Row(modifier = Modifier
                .constrainAs(musicPlayingAnimation) {
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 0.dp)
                }
                .fillMaxWidth()
                .height(15.dp)
            )
            {
                val count = if (isPortraitOrientation()) {
                    3
                } else {
                    10
                }

                val weight = (((1 / count.toFloat()) * 100).roundToInt()) / 100f
                repeat(count) {
                    Box(
                        modifier = Modifier
                            .weight(weight)
                    ) {
                        StationPlayingAnimation()
                    }
                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StationPlayingAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.la_music_equalizer)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true,
        dynamicProperties = rememberLottieDynamicProperties(
            LottieDynamicProperty(
                property = LottieProperty.COLOR_FILTER,
                value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    MaterialTheme.colorScheme.primary.hashCode(),
                    BlendModeCompat.SRC_ATOP
                ),
                keyPath = KeyPath("**")
            )
        )
    )
}

@Preview
@Composable
fun StationInfoPreview() {
    val station = Station(
        id = 1001,
        stationId = 1,
        groupId = 1,
        groupName = "Спокойное",
        name = "Атмосфера",
        url = "https://listen5.myradio24.com/atmo",
        noJingle = false,
        image = "https://topradio.me/assets/image/radio/180/radio-atmosfera.png",
        state = StationPlaybackState.STOPPED,
        smallTitle = "",
        songTitle = "Кузьмин - Я уеду в Комарово"
    )

    StationInfo(
        station = station,
        onStopButtonClicked = {})
}

@Composable
fun isPortraitOrientation(): Boolean {
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

// If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    return when (orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            false //LandscapeContent()
        }

        else -> {
            true //PortraitContent()
        }
    }
}

