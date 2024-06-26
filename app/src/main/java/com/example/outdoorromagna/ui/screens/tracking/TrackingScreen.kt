package com.example.outdoorromagna.ui.screens.tracking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.example.outdoorromagna.utils.PermissionStatus
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackActions
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackState
import com.example.outdoorromagna.ui.screens.home.rememberPermission
import com.example.outdoorromagna.ui.screens.home.requestLocation
import com.example.outdoorromagna.utils.LocationService
import com.example.outdoorromagna.utils.MapPresenter
import com.example.outdoorromagna.utils.observeAsState
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import org.koin.compose.koinInject

@Composable
fun TrackingScreen(
    navController: NavController,
    trackingActions: TrackingActions,
    trackingState: TrackingState,
    user: User,
    tracksDbVm: TracksDbViewModel,
    addTrackActions: AddTrackActions
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val registry = rememberLauncherForActivityResultRegistry()
    val presenter: MapPresenter = remember { MapPresenter(context, registry, MutableLiveData(), tracksDbVm) }
    var isTrackingStarted by remember { mutableStateOf(false) }

    val uiState = remember { MutableLiveData(Ui.EMPTY)}
    val elapsedTimeState = presenter.elapsedTime.observeAsState(0L)
    val mapView = rememberMapViewWithLifecycle()

    presenter.onMapLoaded(context)
    presenter.mySetUi(uiState)
    presenter.onViewCreated(lifecycleOwner)

    val locationService = koinInject<LocationService>()
    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_FINE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                trackingActions.setShowLocationPermissionDenied(true)

            PermissionStatus.PermanentlyDenied ->
                trackingActions.setShowLocationPermissionDenied(true)

            PermissionStatus.Unknown -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        IndicatorsLayout(uiState, elapsedTimeState.value)
        Box(modifier = Modifier.weight(1f)) {
            //MapViewComposable(presenter)
            AndroidView({ mapView }) { mapView ->
                mapView.getMapAsync { googleMap ->
                    Log.d("TAG", "prima setGoogleMap")
                    presenter.setGoogleMap(googleMap)
                    presenter.onViewCreated(lifecycleOwner)
                    Log.d("TAG", "dopo setGoogleMap")
                    googleMap.uiSettings.isZoomControlsEnabled = true
                    presenter.enableUserLocation()
                    requestLocation(locationPermission, locationService)
                }
            }
        }

        Button(
            onClick = {
                isTrackingStarted = !isTrackingStarted
                if (isTrackingStarted) {
                    startTracking(presenter)
                } else {
                    stopTracking(presenter, addTrackActions)
                    navController.navigate(OutdoorRomagnaRoute.AddTrackDetails.currentRoute)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Text(text = if (isTrackingStarted) stringResource(R.string.stop_label) else stringResource(R.string.start_label),
            color = MaterialTheme.colorScheme.onTertiaryContainer)
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                }
                Lifecycle.Event.ON_DESTROY -> {
                    mapView.onDestroy()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun IndicatorsLayout(uiState: MutableLiveData<Ui>, elapsedTimeState: Long) {
    val uiStateValue by uiState.observeAsState(Ui.EMPTY)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        IndicatorRow(
            iconResId = R.drawable.ic_pace,
            description = stringResource(id = R.string.steps),
            label = stringResource(id = R.string.steps),
            value = uiStateValue.formattedSteps
        )
        IndicatorRow(
            iconResId = R.drawable.ic_time,
            description = stringResource(id = R.string.elapsed_time_label),
            label = stringResource(id = R.string.elapsed_time_label),
            value = uiStateValue.formattedTime(elapsedTimeState)
        )
        IndicatorRow(
            iconResId = R.drawable.ic_distance,
            description = stringResource(id = R.string.distance_label),
            label = stringResource(id = R.string.distance_label),
            value = uiStateValue.formattedDistance
        )
    }
}

@Composable
fun IndicatorRow(
    iconResId: Int,
    description: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = description,
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
        )
    }
}

private fun startTracking(presenter: MapPresenter) {
    presenter.startTracking()
}

private fun stopTracking(
    presenter: MapPresenter,
    addTrackActions: AddTrackActions
) {
    presenter.stopTracking(addTrackActions)
}

@Composable
fun rememberLauncherForActivityResultRegistry(): ActivityResultRegistry {
    val context = LocalContext.current as ComponentActivity
    return remember { context.activityResultRegistry }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

data class Ui(
    val formattedSteps: String,
    val distance: Int,
    val steps: Int,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {
    fun formattedTime(elapsedTime: Long): String {
        val minutes = elapsedTime / 60
        val seconds = elapsedTime % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    companion object {

        val EMPTY = Ui(
            formattedSteps = "0 passi",
            distance = 0,
            formattedDistance = "0 metri",
            currentLocation = null,
            userPath = emptyList(),
            steps = 0,
        )
    }
}
