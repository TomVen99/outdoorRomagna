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
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.screens.home.HomeScreenActions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithNavigationDrawerButton(
    onClickNavigationDrawer: () -> Unit,
) {
    TopAppBar(
        title = { Text("Your App Title") },
        navigationIcon = {
            IconButton(onClick = onClickNavigationDrawer) {
                Icon(Icons.Default.Menu, contentDescription = "Open Navigation Drawer")
            }
        }
    )
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController,
    currentRoute: String,
    showSearch: Boolean = false,
    actions: HomeScreenActions? = null,
    drawerState: DrawerState,
    scope: CoroutineScope
    ) {

    /*ModalNavigationDrawer(
        drawerState = drawerState,
        content = {*/
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
                        /*scope.launch {
                            Log.d("TAG", "cliccato menu dentro launch")
                            drawerState.apply {
                                Log.d("TAG", "cliccato menu dentro apply")
                                Log.d("TAG", drawerState.isClosed.toString())
                                if (drawerState.isClosed) {
                                    Log.d("TAG", "open")
                                    drawerState.open()
                                    Log.d("TAG", "open")
                                } else {
                                    drawerState.close()
                                    Log.d("TAG", "close")
                                }
                            }
                        }*/
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                        )
                    }
                },
                actions = {
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
            )/*
        },
        drawerContent = {
            Column {
                // Aggiungi qui il contenuto del drawer
                Text("Navigation Item 1")
                Text("Navigation Item 2")
                Text("Navigation Item 3")
                // Aggiungi ulteriori elementi se necessario
            }
        }
    )*/
}
    /*
@Composable
fun showSearchBar {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            SearchBar { query ->
                performSearch(query = query, context = context) { results ->
                    if (results.isNotEmpty()) {
                        placeLocations = results
                        center = results.first()
                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(center, 12f))
                    }
                }
            }
        }
    }
}*/

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

                Button(
                    onClick = {
                        navController.navigate(OutdoorRomagnaRoute.Tracks.buildRoute(user.username))
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

                Button(
                    modifier = Modifier.padding(end = 10.dp),
                    onClick = {
                        //navController.navigate(OutdoorRomagnaRoute.Profile.route)
                        navController.navigate(OutdoorRomagnaRoute.Profile.buildRoute(user.username))
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

