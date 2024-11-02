package me.kdv.noadsradio.ui.content.station_group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
