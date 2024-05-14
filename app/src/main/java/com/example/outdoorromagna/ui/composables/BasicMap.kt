package com.example.outdoorromagna.ui.composables

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.camera.utils.PermissionHandler
import com.example.camera.utils.PermissionStatus
import com.example.outdoorromagna.utils.LocationService
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

/*data class MapTypes(val mapTypeId: MapType, val title: String, val url: String)

private val mapTypes = listOf(
    MapTypes(MapType.NORMAL, "Default", ""),
    MapTypes(MapType.HYBRID, "Satellite", ""),
    MapTypes(MapType.TERRAIN, "Rilievo", "")
)


@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BasicMap(
    navController: NavHostController,
    latitude: Double,
    longitude: Double,
    actions: MapActions
) {
    var center by remember {
        mutableStateOf(
            LatLng(
                latitude.toDouble(),
                longitude.toDouble()
            )
        )
    }  // Coordinate iniziali di Roma
    var placeLocations by remember { mutableStateOf(listOf<LatLng>()) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(center, 10f, 0f, 0f)
    }
    val context = LocalContext.current
    var showButton by remember { mutableStateOf(false) }
    var showPopUp by remember { mutableStateOf(false) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var mapViewType by remember { mutableStateOf<MapType>(MapType.TERRAIN) }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MapView(
                placeLocations,
                cameraPositionState,
                { showButton = true },
                updateMarkerPosition = { markerPosition = it },
                navController,
                mapViewType
            )

            FloatingActionButton( //bottone del gps
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    requestLocation(locationPermission, locationService)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 30.dp)
                    .size(48.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.GpsFixed, "Use localization")
            }

            FloatingActionButton( //bottone per cambiare la modalità di visualizzazione
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = {
                    showPopUp = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 10.dp)
                    .size(48.dp),
                shape = CircleShape
            ) {
                Icon(Icons.Outlined.Layers, "Choose map type")
            }

            if (showPopUp)
                ModalBottomSheet(
                    onDismissRequest = { showPopUp = false }
                ) {
                    Column(modifier = Modifier.padding(bottom = 50.dp)) {
                        mapTypes.forEach { type ->
                            Row {
                                Button(onClick = {
                                    mapViewType = type.mapTypeId
                                    showPopUp = false
                                }) {
                                    Text(text = type.title)
                                }
                            }
                        }
                    }
                }

            /*if (showButton) { //se clicca su un punto della mappa
                addLocation()*/

        }
    }
}*/

@Composable
fun addLocation() {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        onClick = { },
        modifier = Modifier
            .padding(start = 16.dp, bottom = 32.dp)
    ) {
        Row (modifier = Modifier.padding(8.dp)){
            Icon(Icons.Outlined.Add, "Share Travel")
            Text(text = "Aggiungi location")
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
    navController: NavHostController,
    mapView: MapType
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = mapView))
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
            updateMarkerPosition(markerPosition)  // Aggiorna la posizione ogni volta che viene cliccato un nuovo punto
        },
        properties = properties,
        uiSettings = uiSettings,
        onMapLoaded = { }
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
