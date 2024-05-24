package com.example.outdoorromagna.ui.screens.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GpsFixed
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.camera.utils.PermissionHandler
import com.example.camera.utils.PermissionStatus
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.repositories.generateTestTracks
import com.example.outdoorromagna.ui.GroupedTracksState
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.TracksDbState
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.getMyDrawerState
import com.example.outdoorromagna.ui.screens.tracks.TracksState
import com.example.outdoorromagna.utils.LocationService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
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
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.compose.koinInject

data class MapTypes(val mapTypeId: MapType, val title: String, val drawableId: Int)

data class PlaceDetails(val latLng: LatLng, val name: String)

val mapTypes = listOf(
    MapTypes(MapType.NORMAL, "Default", R.drawable.defaultmap),
    MapTypes(MapType.HYBRID, "Satellite", R.drawable.satellitemap),
    MapTypes(MapType.TERRAIN, "Rilievo", R.drawable.reliefmap)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    state: HomeScreenState,
    actions: HomeScreenActions,
    user : User,
    tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,
    groupedTracksState: GroupedTracksState,
    tracksState: TracksDbState
) {
    val scope = rememberCoroutineScope()
    Log.d("grouped", groupedTracksState.toString())
    Log.d("Tutti i track", tracksDbState.tracks.map { track -> track.id }.toString())

    /**PER ELIMINARE TUTTI I TRACK*/
    /*tracksState.tracks.forEach { track ->
        tracksDbVm.deleteTrack(track)
    }*/
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
                var placeSearch by remember { mutableStateOf(listOf<PlaceDetails>()) }
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
                            cameraPositionState,
                            navController,
                            user,
                            state.mapView,
                            tracksDbState,
                            groupedTracksState,
                            tracksDbVm
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
                                /**PER INSERIRE I TRACK DI TEST*/
                                if (tracksState.tracks.isEmpty()) {
                                    val testTracks = generateTestTracks()
                                    testTracks.forEach { testTrack ->
                                        tracksDbVm.addTrack(testTrack)
                                    }
                                }
                                showPopUp = true
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 10.dp, top = 8.dp)
                                .size(48.dp),
                            shape = CircleShape
                        ) {
                            Icon(Icons.Outlined.Layers, "Choose map type")
                        }

                        if (showPopUp)
                            Dialog(
                                onDismissRequest = { showPopUp = false }
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.background
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    shape = RoundedCornerShape(16.dp),
                                ) {
                                    Text(text = "Tipo di mappa:", modifier = Modifier.padding(10.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                    ) {

                                        mapTypes.forEach { type ->
                                            Button(
                                                onClick = {
                                                    actions.setMapView(type.mapTypeId)
                                                    showPopUp = false
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color.Transparent,
                                                    contentColor = MaterialTheme.colorScheme.primary
                                                ),
                                                shape = RectangleShape,
                                                modifier = Modifier.padding(0.dp),
                                                contentPadding = PaddingValues(0.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = type.drawableId),
                                                    contentDescription = type.title,
                                                    modifier = Modifier
                                                        .size(70.dp)
                                                        .padding(0.dp)
                                                        .border(
                                                            BorderStroke(
                                                                1.dp,
                                                                MaterialTheme.colorScheme.onBackground
                                                            )
                                                        )
                                                )
                                            }
                                        }
                                    }
                                Spacer(modifier = Modifier.size(10.dp))
                                }
                            }
                        if (state.showSearchBar) {
                            Column {
                                Row {
                                    SearchBar(actions = actions, onQueryChanged =  { query ->
                                            performSearch(query = query, context = context) { results ->
                                                if (results.isNotEmpty()) {
                                                    placeSearch = results
                                                }
                                            }
                                        }
                                    )
                                }
                                if(placeSearch.isNotEmpty()) {
                                    placeSearch.forEach { place ->
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 0.dp)
                                                .fillMaxWidth()
                                                .background(MaterialTheme.colorScheme.background)
                                                .border(
                                                    BorderStroke(
                                                        1.dp,
                                                        MaterialTheme.colorScheme.onBackground
                                                    ),
                                                    shape = RoundedCornerShape(
                                                        0.dp,
                                                        0.dp,
                                                        4.dp,
                                                        4.dp
                                                    )
                                                )
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    center = place.latLng
                                                    cameraPositionState
                                                        .move(CameraUpdateFactory.newLatLngZoom(center,12f))
                                                    actions.setShowSearchBar(false)
                                                    placeSearch = listOf()
                                                },
                                                colors = ButtonDefaults.textButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.background,
                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                ),
                                                modifier = Modifier
                                                    .padding(vertical = 0.dp)
                                                    .fillMaxWidth(),
                                                shape = RectangleShape,
                                                contentPadding = PaddingValues(0.dp),
                                            ) {
                                                Text(text = place.name)
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            placeSearch = emptyList()
                        }
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

fun requestLocation(locationPermission: PermissionHandler, locationService: LocationService) {
    if (locationPermission.status.isGranted) {
        locationService.requestCurrentLocation()
    } else {
        locationPermission.launchPermissionRequest()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MapView(
    cameraPositionState: CameraPositionState,
    navController: NavHostController,
    user : User,
    mapView: MapType,
    tracksDbState: TracksDbState,
    groupedTracksState: GroupedTracksState,
    tracksDbVm: TracksDbViewModel
) {
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = {  },
        properties = MapProperties(mapType = mapView)
    ) {
        groupedTracksState.tracks.forEach { location ->
            Marker(
                state = MarkerState(position = LatLng(location.groupedLat, location.groupedLng)),
                onClick = {

                    tracksDbVm.getTracksInRange(location.groupedLat, location.groupedLng)
                    navController.navigate(
                        OutdoorRomagnaRoute.Tracks.buildRoute(user.username, true)
                    )
                    true
                }
            )
        }
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
private fun SearchBar(onQueryChanged: (String) -> Unit, actions: HomeScreenActions) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        label = { Text("Cerca luogo") },
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        singleLine = true,
        shape = RectangleShape,
        trailingIcon = {
            IconButton(onClick = { actions.setShowSearchBar(false) }) {
                Icon(Icons.Outlined.Close, contentDescription = "Chiudi")
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background
        )
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
