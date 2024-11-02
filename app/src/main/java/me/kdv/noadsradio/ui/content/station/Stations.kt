package me.kdv.noadsradio.ui.content.station

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.kdv.noadsradio.domain.model.Station
import me.kdv.noadsradio.ui.content.station_group.animateScrollAndCentralizeItem

@Composable
fun Stations(
    stationList: List<Station>,
    movedStationGroupId: Int,
    onStationClickListener: (Station) -> Unit,
    onStationScrollListener: (Station) -> Unit
) {
    val listState = rememberLazyListState()

    val nested = object : NestedScrollConnection {

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            val index = if (consumed.y > 0) {
                listState.getFirstItemVisibleIndex
            } else {
                listState.getLastItemVisibleIndex
            }
            onStationScrollListener.invoke(stationList[index])
            return Offset.Zero
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nested)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(items = stationList) { station ->
                StationItem(station = station) {
                    onStationClickListener(it)
                }
            }
        }
    }

    if (movedStationGroupId > 0) {
        val coroutineScope = rememberCoroutineScope()
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