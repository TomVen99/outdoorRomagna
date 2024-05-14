package com.example.outdoorromagna.ui.composables

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    navController: NavHostController,
    currentRoute: String,
    showSearch: Boolean = false,
    actions: HomeScreenActions? = null
) {
    //var showSearch by remember { mutableStateOf<Boolean>(false) }
    TopAppBar(
        title = {
            Text(
                currentRoute,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                )
            }
        },
        actions = {
            if(showSearch) {
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
                    onClick = { /*TODO*/ },
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
                    onClick = { /*TODO*/ },
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

