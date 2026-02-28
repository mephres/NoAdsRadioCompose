package me.kdv.noadsradio.ui.content.station

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import me.kdv.noadsradio.R
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.domain.model.StationPlaybackState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StationItem(
    station: Station,
    onStationClick: (Station) -> Unit
) {
    var hasLoadingError by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 4.dp
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            val (
                stationImage, stationTitleText, stationGenreText,
                stationJingleImage, stationControlButton, stationProgressBar
            ) = createRefs()

            val boxColor = if (hasLoadingError)
                MaterialTheme.colorScheme.onBackground
            else
                MaterialTheme.colorScheme.background

            Box(
                modifier = Modifier
                    .constrainAs(stationImage) {
                        start.linkTo(parent.start, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
                    .size(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = boxColor),

                contentAlignment = Alignment.Center
            ) {
                if (hasLoadingError) {
                    Text(
                        text = station.name.take(2),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(station.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.description),
                        contentScale = ContentScale.Crop,
                        onError = {
                            hasLoadingError = true
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                }
            }
            Text(
                text = station.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(stationTitleText) {
                    start.linkTo(stationImage.end, margin = 8.dp)
                    end.linkTo(stationJingleImage.start, margin = 0.dp, goneMargin = 8.dp)
                    top.linkTo(stationImage.top, margin = 0.dp)
                    width = Dimension.fillToConstraints
                }.basicMarquee(),
                softWrap = false,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = station.groupName,
                modifier = Modifier.constrainAs(stationGenreText) {
                    start.linkTo(stationImage.end, margin = 8.dp)
                    top.linkTo(stationTitleText.bottom, margin = 4.dp)
                }, fontSize = 11.sp
            )
            IconButton(
                modifier = Modifier
                    .constrainAs(stationControlButton) {
                        end.linkTo(parent.end, margin = 8.dp)
                        top.linkTo(parent.top, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    }
                    .padding(0.dp)
                    .size(50.dp),
                onClick = {
                    onStationClick(station)
                }) {
                val resourceId = when (station.state) {
                    StationPlaybackState.PLAYING -> {
                        R.drawable.ic_stop_circle_48
                    }

                    StationPlaybackState.LOADED -> {
                        R.drawable.ic_circle_48
                    }

                    StationPlaybackState.STOPPED -> {
                        R.drawable.ic_play_circle_48
                    }
                }
                Icon(
                    painter = painterResource(resourceId),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .constrainAs(stationJingleImage) {
                        end.linkTo(stationControlButton.start, margin = 0.dp)
                        top.linkTo(stationControlButton.top, margin = 8.dp)
                        bottom.linkTo(stationControlButton.bottom, margin = 8.dp)
                        visibility = if (station.noJingle)
                            Visibility.Visible
                        else
                            Visibility.Gone
                    }
                    .size(50.dp)
                    .background(color = Color.Transparent)
            ) {
                StationJingleAnimation()
            }
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(stationProgressBar) {
                        end.linkTo(stationControlButton.end, margin = 8.dp)
                        top.linkTo(stationControlButton.top, margin = 8.dp)
                        bottom.linkTo(stationControlButton.bottom, margin = 8.dp)
                        start.linkTo(stationControlButton.start, margin = 8.dp)
                        val visibleProgressIndicator =
                            if (station.state == StationPlaybackState.LOADED) {
                                Visibility.Visible
                            } else {
                                Visibility.Invisible
                            }
                        visibility = visibleProgressIndicator
                    }
                    .size(25.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StationJingleAnimation() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.la_music_jingle)
    )

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )
}

@Preview
@Composable
fun StationItemPreview() {

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
        smallTitle = ""
    )

    StationItem(station = station, onStationClick = {})
}