package com.example.outdoorromagna.ui.screens.tracks

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.FilterBar
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.getMyDrawerState


@Composable
fun TracksScreen(
    navController: NavHostController,
    user: User,
    //onModify: (User) -> Unit,
    state: TracksState,
    actions: TracksActions,
    viewModel : UsersViewModel,
    trackList: List<Track>
) {
    /*val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)*/
    Log.d("trackList", trackList.toString())
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
                    showFilter = true,
                    filterState = state
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            Column(

                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("TAG", "showFilterBar " + state.showFilterBar.toString())
                if (state.showFilterBar) {
                    Column {
                        Row {
                            FilterBar(actions = actions)

                        }
                    }
                }
                // qui si dovrà fare richiesta al database
                val items = listOf("Pippo", "pluto", "paperino")
                val trackItems = listOf<TrackItem>(
                    TrackItem("Pippo", true, "prima pippo"),
                    TrackItem("pluto", false, "prima pluto"),
                    TrackItem("paperino", true, "prima paperino"))

                trackItems.forEach() { item ->
                    ListItem(
                        headlineContent = { Text(text= item.title) },
                        supportingContent = {
                            item.shortDescription?.let { Text(text = it) }
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                item.isFavorite = !item.isFavorite
                                Log.d("TAG", "addFavorite")
                            })
                            {
                                Icon(imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Add to favorites")
                            }
                        },
                    )
                }
            }

        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )
}

