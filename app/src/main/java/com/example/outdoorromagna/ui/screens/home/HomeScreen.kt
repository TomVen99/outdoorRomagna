package com.example.outdoorromagna.ui.screens.home

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.ImageWithPlaceholder
import com.example.outdoorromagna.ui.composables.Size
import com.example.camera.utils.PermissionHandler
import com.example.camera.utils.PermissionStatus
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.screens.sideBarMenu.SideBarMenu
import com.example.outdoorromagna.ui.screens.sideBarMenu.getMyDrawerState
import com.example.outdoorromagna.utils.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
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
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.compose.koinInject

data class MapTypes(val mapTypeId: MapType, val title: String, val url: String)

data class PlaceDetails(val latLng: LatLng, val name: String)

val mapTypes = listOf(
    MapTypes(MapType.NORMAL, "Default", ""),
    MapTypes(MapType.HYBRID, "Satellite", ""),
    MapTypes(MapType.TERRAIN, "Rilievo", "")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    state: HomeScreenState,
    actions: HomeScreenActions,
    user : User
) {
    //val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    //Log.d("TAG", "Home" + drawerState.toString())
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "OutdoorRomagna",
                    actions = actions,
                    showSearch = true,
                    drawerState = getMyDrawerState(),
                    scope = scope
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                //CreateMap(navController, state, actions)
                var center by remember {
                    mutableStateOf(
                        LatLng(
                            44.1528f.toDouble(),
                            12.2036f.toDouble()
                        )
                    )
                }
                var placeLocations by remember { mutableStateOf(listOf<PlaceDetails>()) }
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition(center, 10f, 0f, 0f)
                }
                val context = LocalContext.current
                var showButton by remember { mutableStateOf(false) }
                var showPopUp by remember { mutableStateOf(false) }
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MapView(
                            placeLocations.map { x -> x.latLng },
                            cameraPositionState,
                            { showButton = true },
                            updateMarkerPosition = { markerPosition = it },
                            navController,
                            state.mapView
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
                                                actions.setMapView(type.mapTypeId)
                                                showPopUp = false
                                            }) {
                                                Text(text = type.title)
                                            }
                                        }
                                    }
                                }
                            }
                        if (state.showSearchBar) {
                            SearchBar { query ->
                                performSearch(query = query, context = context) { results ->
                                    if (results.isNotEmpty()) {
                                        placeLocations = results
                                        Log.d("tag", results.toString())
                                        center = results.first().latLng //forse da togliere
                                        cameraPositionState.move(
                                            CameraUpdateFactory.newLatLngZoom(
                                                center,
                                                12f
                                            )
                                        ) //forse da togliere
                                    }
                                }
                            }
                            /*if(placeLocations.isNotEmpty()) {
                            DropdownMenu(
                                modifier = Modifier.fillMaxWidth(),
                                expanded = true,
                                onDismissRequest = { /*actions.setShowSearchBar(false)*/ }) {
                                placeLocations.forEach { place ->
                                    DropdownMenuItem(text = { place.name }, onClick = { /*TODO*/ })
                                }
                            }
                        }*/
                        }


                        /*if (showButton) { //se clicca su un punto della mappa
                        addLocation()*/

                    }
                }

            }
        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )
}


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
            Text(text = "Aggiungi percorso")
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
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
            updateMarkerPosition(markerPosition)  // Aggiorna la posizione ogni volta che viene cliccato un nuovo punto
        },
        properties = MapProperties(mapType = mapView)
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

@Composable
private fun SearchBar(onQueryChanged: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        label = { Text("Cerca luogo") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    )
}

private fun performSearch(query: String, context: Context, onResult: (List<PlaceDetails>) -> Unit) {
    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyB6IrzeS3vCCiPHToNAG5u0tZkIyJx1IbM")
    }
    val placesClient = Places.createClient(context)
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
        val locations = mutableListOf<PlaceDetails>()
        for (prediction in response.autocompletePredictions) {
            fetchPlaceDetails(prediction.placeId, placesClient) { latLng ->
                latLng?.let {
                    locations.add(PlaceDetails(it, prediction.getFullText(null).toString()))
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

//usata per richiedere la latlng
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
