package com.example.outdoorromagna.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.example.outdoorromagna.ui.PlacesState
import com.example.outdoorromagna.ui.composables.BasicMap
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
    /*Scaffold( //icona del +
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { navController.navigate(TravelDiaryRoute.AddTravel.route) }
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
            }
        } else {
            NoItemsPlaceholder(Modifier.padding(contentPadding), navController)
        }
    }*/
    CreateMap(navController, state, actions)
}


@Composable
fun CreateMap(navController: NavHostController, state: HomeScreenState, actions: MapActions) {
    val singapore = LatLng(1.35, 103.87)
    /*val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }*/
    BasicMap(navController = navController, latitude = 1.35, longitude = 103.87, actions)
}
