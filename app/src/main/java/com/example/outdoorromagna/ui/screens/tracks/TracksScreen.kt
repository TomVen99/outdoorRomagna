package com.example.outdoorromagna.ui.screens.tracks

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbState
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.FilterBar
import com.example.outdoorromagna.ui.composables.FilterOption
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.getMyDrawerState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TracksScreen(
    navController: NavHostController,
    user: User,
    state: TracksState,
    actions: TracksActions,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,
    showFilter: Boolean,
) {
    val specificTracksList by tracksDbVm.specificTracksList.observeAsState()
    var actualFilterOption by remember { mutableIntStateOf(FilterOption.ALL_TRACKS.ordinal) }

    Log.d("trackList", specificTracksList.toString())
    val scope = rememberCoroutineScope()
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "OutdoorRomagna",
                    showSearch = false,
                    drawerState = getMyDrawerState(),
                    trackActions = actions,
                    scope = scope,
                    showFilter = showFilter,
                    filterState = state
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (state.showFilterBar) {
                    Log.d("tracks", tracksDbState.tracks.toString())
                    Log.d("specifictrackList", specificTracksList.toString())
                    stickyHeader {
                        Box (
                            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(top = 5.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.onPrimaryContainer),
                                verticalArrangement = Arrangement.Top
                            ) {
                                Row(
                                ) {
                                    FilterBar(
                                        actions = actions,
                                        filterOption = actualFilterOption,
                                    )
                                }
                            }
                        }
                    }
                    actualFilterOption = when(state.filter) {
                        FilterOption.YOUR_TRACKS -> {
                            tracksDbVm.getUserTracks(user.id)
                            FilterOption.YOUR_TRACKS.ordinal
                        }

                        FilterOption.ALL_TRACKS -> {
                            tracksDbVm.resetSpecificTracks()
                            FilterOption.ALL_TRACKS.ordinal
                        }
                    }
                }
                items(getTrackListToPrint(specificTracksList, tracksDbState.tracks)) { item ->
                    PrintListItems(item) {
                        navController.navigate(
                            OutdoorRomagnaRoute.TrackDetails.buildRoute(
                                user.username,
                                item.id
                            )
                        )
                    }
                }
            }
        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController,
        tracksDbState,
    )
}

fun getTrackListToPrint(specificTracksList: List<Track>?, tracksState: List<Track>) : List<Track> {
    if (specificTracksList != null)
        return specificTracksList
    return tracksState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintListItems(track: Track, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        ListItem(
            headlineContent = { Text(text= track.name) },
            supportingContent = {
                Text(text = track.description)
            },
        )
    }
}


