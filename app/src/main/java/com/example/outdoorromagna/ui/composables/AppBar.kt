package com.example.outdoorromagna.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Signpost
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.screens.home.HomeScreenActions
import com.example.outdoorromagna.ui.screens.tracks.TracksActions
import com.example.outdoorromagna.ui.screens.tracks.TracksState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController,
    currentRoute: String,
    showSearch: Boolean = false,
    actions: HomeScreenActions? = null,
    trackActions: TracksActions? = null,
    filterState: TracksState? = null,
    drawerState: DrawerState,
    scope: CoroutineScope,
    showFilter: Boolean = false,
    showLogout: Boolean = false
) {
    Log.d("TAG", drawerState.toString())
    TopAppBar(
        title = {
            Text(
                currentRoute,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                Log.d("TAG", "cliccato menu")
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            }
        },
        actions = {
            if (showLogout) {
                IconButton(onClick = {
                    navController.navigate(OutdoorRomagnaRoute.Login.route)
                })
                {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }
            if (showFilter) {
                IconButton(onClick = {
                    if (filterState?.isShowFilterEnabled == true) {
                        trackActions?.setShowFilter(false)
                    } else {
                        trackActions?.setShowFilter(true)
                        Log.d("TAG", "cliccato filtro")
                    }
                }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Filtra"
                    )
                }
            }
            if (showSearch) {
                IconButton(onClick = { actions?.setShowSearchBar(true) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Cerca"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun BottomAppBar(
    navController: NavHostController,
    user: User
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = {
                        navController.navigate(
                            OutdoorRomagnaRoute.Home.buildWithoutPosition(
                                user.username
                            )
                        )
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Map,
                            contentDescription = "Mappa"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Mappa")
                    }
                }

                OutdoorRomagnaRoute.Tracks.buildRoute(user.username)
                OutdoorRomagnaRoute.Settings.buildRoute(user.username)
                Button(
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.Tracks.currentRoute)
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Signpost,
                            contentDescription = "Percorsi"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Percorsi")
                    }
                }

                OutdoorRomagnaRoute.Profile.buildRoute(user.username)
                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.Profile.currentRoute)
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profilo"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Profilo")
                    }
                }
            }
        }
    )
}

