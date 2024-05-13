package com.example.outdoorromagna.ui.composables

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.camera.utils.PermissionHandler
import com.example.camera.utils.PermissionStatus
import com.example.outdoorromagna.ui.screens.home.MapActions
import com.example.outdoorromagna.utils.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.compose.koinInject

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BasicMap(
    navController: NavHostController,
    latitude: Double,
    longitude: Double,
    actions: MapActions
) {
    var center by remember { mutableStateOf(LatLng(latitude.toDouble(), longitude.toDouble())) }  // Coordinate iniziali di Roma
    var placeLocations by remember { mutableStateOf(listOf<LatLng>()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(center, 10f, 0f, 0f)
    }
    val context = LocalContext.current
    var showButton by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }


    val locationService = koinInject<LocationService>()

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                actions.setShowLocationPermissionDeniedAlert(true)

            PermissionStatus.PermanentlyDenied ->
                actions.setShowLocationPermissionPermanentlyDeniedSnackbar(true)

            PermissionStatus.Unknown -> {}
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
                MapView(placeLocations, cameraPositionState, {
                    showButton = true
                }, updateMarkerPosition = {
                    markerPosition = it
                },
                    navController
                )
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    requestLocation(locationPermission, locationService)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 30.dp)
            ) {
                Icon(Icons.Outlined.GpsFixed, "Use localization")
            }
            /*if (showButton) { //se clicca su un punto della mappa
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 32.dp)
                ) {
                    Row (modifier = Modifier.padding(8.dp)){
                        Icon(Icons.Outlined.Add, "Share Travel")
                        Text(text = "Aggiungi location")
                    }
                }
            }*/

        }
    }
}

fun requestLocation(locationPermission: PermissionHandler, locationService: LocationService) {
    if (locationPermission.status.isGranted) {
        locationService.requestCurrentLocation()
    } else {
        locationPermission.launchPermissionRequest()
    }
}

@Composable
fun MapView(
    placeLocations: List<LatLng>,
    cameraPositionState: CameraPositionState,
    onMarkerClick: () -> Unit,
    updateMarkerPosition: (LatLng?) -> Unit,
    navController: NavHostController
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }  // Aggiunge lo stato per memorizzare la posizione del marker

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
            updateMarkerPosition(markerPosition)  // Aggiorna la posizione ogni volta che viene cliccato un nuovo punto
        }
    ) {
        // Visualizza il marker nella posizione memorizzata
        markerPosition?.let {
            Marker(
                state = MarkerState(position = it),
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)  // Imposta il marker di colore giallo
            )
            onMarkerClick()
        }

        // Visualizza tutti i markers delle posizioni di ricerca
        placeLocations.forEach { location ->
            Marker(state = MarkerState(position = location))
        }

        /*state.markers.forEach{marker ->
            Marker(
                state = MarkerState(LatLng(marker.latitude.toDouble(), marker.longitude.toDouble())),
                icon =
                if (isFavorite(marker))
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                else
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                onClick = {
                    navController.navigate(TravelDiaryRoute.HomeMarkDetail.buildRoute(
                        user.username,
                        marker.latitude,
                        marker.longitude
                    ))
                    true
                }
            )
        }*/
    }
}

private fun performSearch(query: String, context: Context, onResult: (List<LatLng>) -> Unit) {
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
    }
    val placesClient = Places.createClient(context)
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        val locations = mutableListOf<LatLng>()
        for (prediction in response.autocompletePredictions) {
            fetchPlaceDetails(prediction.placeId, placesClient) { latLng ->
                latLng?.let {
                    locations.add(it)
                    if (locations.size == response.autocompletePredictions.size) {
                        onResult(locations)
                    }
                }
            }
        }
    }.addOnFailureListener { exception ->
        Log.e("SearchPlaces", "Error fetching autocomplete predictions", exception)
    }
}

private fun fetchPlaceDetails(placeId: String, placesClient: PlacesClient, onResult: (LatLng?) -> Unit) {
    val placeFields = listOf(Place.Field.LAT_LNG)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request).addOnSuccessListener { fetchPlaceResponse ->
        onResult(fetchPlaceResponse.place.latLng)
    }.addOnFailureListener { exception ->
        Log.e("FetchPlaceDetails", "Error fetching place details", exception)
        onResult(null)
    }
}

@Composable
fun rememberPermission(
    permission: String,
    onResult: (status: PermissionStatus) -> Unit = {}
): PermissionHandler {
    var status by remember { mutableStateOf(PermissionStatus.Unknown) }

    val activity = (LocalContext.current as ComponentActivity)

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        status = when {
            isGranted -> PermissionStatus.Granted
            activity.shouldShowRequestPermissionRationale(permission) -> PermissionStatus.Denied
            else -> PermissionStatus.PermanentlyDenied
        }
        onResult(status)
    }

    val permissionHandler by remember {
        derivedStateOf {
            object : PermissionHandler {
                override val permission = permission
                override val status = status
                override fun launchPermissionRequest() = permissionLauncher.launch(permission)
            }
        }
    }
    return permissionHandler
}
