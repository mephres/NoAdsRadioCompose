package me.kdv.noadsradio.ui.content.station

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import me.kdv.noadsradio.presentation.station_group.StationGroupComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationGroupContent(component: StationGroupComponent) {

    val state by component.model.collectAsState()

    val title by remember {
        mutableStateOf(state.currentStationGroup?.description ?: "")
    }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = title)
                },

                navigationIcon = {
                    IconButton(onClick = {
                        component.onBackClick()
                    }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .wrapContentHeight()
                        .fillMaxSize()
                ) {
                    val (stations) = createRefs()
                    LazyColumn(modifier = Modifier.constrainAs(stations) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                        height = Dimension.fillToConstraints
                    }) {
                        items(state.stations) { station ->
                            StationItem(station = station) {

                            }
                        }
                    }
                }
            }
        }
    )
}