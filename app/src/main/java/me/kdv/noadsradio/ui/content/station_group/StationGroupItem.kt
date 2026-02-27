package me.kdv.noadsradio.ui.content.station_group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.kdv.noadsradio.domain.model.StationGroup

@Composable
fun StationGroupItem(
    stationGroup: StationGroup,
    onStationGroupSelect: (StationGroup) -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {
                onStationGroupSelect.invoke(stationGroup)
            }
            .size(128.dp)
            .background(
                color = Color.Red, // MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        val stationColor = if (stationGroup.isCurrent) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.primary
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.TopCenter),
            text = stationGroup.description.uppercase(),
            color = stationColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Preview
@Composable
fun StationGroupPreview() {
    val stationGroup = StationGroup(
        id = 0, name = "Singers", description = "Исполнители"
    )
    StationGroupItem(stationGroup = stationGroup, onStationGroupSelect = {})
}
