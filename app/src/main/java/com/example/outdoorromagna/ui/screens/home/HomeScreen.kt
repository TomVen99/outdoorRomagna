package com.example.outdoorromagna.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.Place
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.composables.ImageWithPlaceholder
import com.example.outdoorromagna.ui.composables.Size
import com.example.outdoorromagna.ui.composables.BasicMap
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    state: HomeScreenState,
    actions: MapActions) {
    Scaffold(
        topBar = { TopAppBar(navController, "OutdoorRomagna") },
        bottomBar = { BottomAppBar(navController) },
    ){
        contentPadding ->
        Column (
            modifier = Modifier.padding(contentPadding).fillMaxSize()
        ){
            CreateMap(navController, state, actions)
    }

    }/*
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(OutdoorRomagnaRoute.AddTravel.route) }
            ) {
                Icon(Icons.Outlined.Add, "Add Travel")
            }
        },
    ) { contentPadding -> if (state.places.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 80.dp),
                modifier = Modifier.padding(contentPadding)
            ) {
                items(state.places) { item ->
                    TravelItem(
                        item,
                        onClick = {
                            navController.navigate(OutdoorRomagnaRoute.TravelDetails.buildRoute(item.id.toString()))
                        }
                    )
                }
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding), navController)
        }
    }*/
    //CreateMap(navController, state, actions)
}

@Composable
fun CreateMap(navController: NavHostController, state: HomeScreenState, actions: MapActions) {
    val singapore = LatLng(1.35, 103.87)
    /*val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }*/
    BasicMap(navController = navController, latitude = 1.35, longitude = 103.87, actions)
}
