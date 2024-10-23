package me.kdv.noadsradio.presentation.station_groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.StationGroup

@Composable
fun StationGroupItem(
    stationGroup: StationGroup,
    onStationGroupClickListener: (StationGroup) -> Unit
) {
    Box(
        modifier = Modifier.padding(
            horizontal = 16.dp, vertical = 8.dp
        )
    ) {
        val stationColor = if (stationGroup.isCurrent) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.primary
        }
        Text(
            modifier = Modifier.clickable {
                onStationGroupClickListener.invoke(stationGroup)
            },
            text = stationGroup.description.uppercase(),
            color = stationColor,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold, fontSize = 16.sp
            )
        )
    }
}

@Preview
@Composable
fun StationGroupPreview() {
    val stationGroup = StationGroup(
        id = 0, name = "Singers", description = "Исполнители"
    )
    StationGroupItem(stationGroup = stationGroup, onStationGroupClickListener = {})
}
