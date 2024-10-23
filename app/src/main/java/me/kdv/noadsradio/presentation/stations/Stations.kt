package me.kdv.noadsradio.presentation.stations

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.presentation.station_groups.animateScrollAndCentralizeItem
import me.kdv.noadsradio.ui.content.station.StationItem

@Composable
fun Stations(
    stationList: List<Station>,
    movedStationGroupId: Int?,
    onStationClickListener: (Station) -> Unit,
    onStationScrollListener: (Station) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState)

    val nested = object : NestedScrollConnection {

        override fun onPreScroll(
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            val index = if (listState.isLastItemVisible)
                scrollContext.lastIndex
            else if (listState.isFirstItemVisible)
                scrollContext.firstIndex
            else
                (scrollContext.lastIndex - scrollContext.firstIndex) / 2 + scrollContext.firstIndex

            onStationScrollListener(stationList[index])

            Log.d("Stations->onStationScrollListener", "$available.y")
            return Offset.Zero
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .wrapContentHeight()
            .nestedScroll(nested),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = stationList) { station ->
            StationItem(station = station) {
                onStationClickListener(it)
            }
        }
    }

    val coroutineScope = rememberCoroutineScope()

    movedStationGroupId?.let {
        val station = stationList.firstOrNull {
            movedStationGroupId == it.groupId
        }

        station?.let {
            val index = stationList.indexOf(it)
            coroutineScope.launch {
                listState.animateScrollAndCentralizeItem(index)
            }
        }
    }
}

val LazyListState.isLastItemVisible: Boolean
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0

val LazyListState.getLastItemVisibleIndex: Int
    get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

val LazyListState.getFirstItemVisibleIndex: Int
    get() = firstVisibleItemIndex

data class ScrollContext(
    val isTop: Boolean,
    val isBottom: Boolean,
    val firstIndex: Int,
    val lastIndex: Int,
    val preIndex: Int = 0
)

@Composable
fun rememberScrollContext(listState: LazyListState): ScrollContext {
    val scrollContext by remember {
        derivedStateOf {
            ScrollContext(
                isTop = listState.isFirstItemVisible,
                isBottom = listState.isLastItemVisible,
                firstIndex = listState.getFirstItemVisibleIndex,
                lastIndex = listState.getLastItemVisibleIndex
            )
        }
    }
    return scrollContext
}