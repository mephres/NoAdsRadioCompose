package me.kdv.noadsradio.ui.content.station_group

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.StationGroup

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun StationGroups(
    stationGroupList: List<StationGroup>,
    onStationGroupClickListener: (StationGroup) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items = stationGroupList) { stationGroup ->
            StationGroupItem(stationGroup = stationGroup) {
                onStationGroupClickListener(it)
                coroutineScope.launch {
                    val index = stationGroupList.indexOf(it)
                    listState.animateScrollAndCentralizeItem(index = index)
                }
            }
            if (stationGroup.isCurrent) {
                coroutineScope.launch {
                    listState.animateScrollAndCentralizeItem(index = stationGroupList.indexOf(stationGroup))
                }
            }
        }
    }
}

suspend fun LazyListState.animateScrollAndCentralizeItem(index: Int) {
    val itemInfo = this.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
    if (itemInfo != null) {
        val center = layoutInfo.viewportEndOffset / 2
        val childCenter = itemInfo.offset + itemInfo.size / 2
        animateScrollBy((childCenter - center).toFloat())
    } else {
        animateScrollToItem(index)
    }
}

@Preview
@Composable
fun StationGroupsPreview() {
    val list = listOf<StationGroup>(
        StationGroup(id = 0, name = "Singers", description = "Исполнители"),
        StationGroup(id = 1, name = "Rock", description = "Рок", isCurrent = true),
        StationGroup(id = 2, name = "Soft", description = "Спокойное"),
        StationGroup(id = 3, name = "Pop", description = "Поп"),
        StationGroup(id = 4, name = "Retro", description = "Ретро"),
        StationGroup(id = 5, name = "Dance", description = "Танцевальная"),
    )
    StationGroups(list, onStationGroupClickListener = {})
}