package com.example.outdoorromagna.ui.screens.addtrack

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.camera.utils.PermissionStatus
import com.example.camera.utils.rememberPermission
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.data.remote.OSMDataSource
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TrackingActivity
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.ImageWithPlaceholder
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.Size
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.getMyDrawerState
import com.example.outdoorromagna.utils.LocationService
import com.example.outdoorromagna.utils.rememberCameraLauncher
import org.koin.compose.koinInject

@Composable
fun AddTrackScreen(
    state: AddTrackState,
    actions: AddTrackActions,
    onSubmit: () -> Unit,
    navController: NavHostController,
    user: User,
    activity: Activity
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Camera

    /*val cameraLauncher = rememberCameraLauncher { imageUri ->
        actions.setImageUri(imageUri)
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }*/

    // Location

    /*val locationService = koinInject<LocationService>()

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

    fun requestLocation() {
        if (locationPermission.status.isGranted) {
            locationService.requestCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationService.isLocationEnabled) {
        actions.setShowLocationDisabledAlert(locationService.isLocationEnabled == false)
    }*/

    // HTTP

    /*val osmDataSource = koinInject<OSMDataSource>()

    fun isOnline(): Boolean {
        val connectivityManager = ctx
            .applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }
    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    LaunchedEffect(locationService.coordinates) {
        if (locationService.coordinates == null) return@LaunchedEffect
        if (!isOnline()) {
            actions.setShowNoInternetConnectivitySnackbar(true)
            return@LaunchedEffect
        }
        val place = osmDataSource.getPlace(locationService.coordinates!!)
        actions.setDestination(place.displayName)
    }*/

    // UI
    var gpsChecker by rememberSaveable { mutableStateOf(checkGPS(context)) }
    var internetConnChecker by rememberSaveable { mutableStateOf(checkInternet(context)) }
    val scope = rememberCoroutineScope()
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = OutdoorRomagnaRoute.AddTrack.title,
                    drawerState = getMyDrawerState(),
                    scope = scope
                )
            },
            bottomBar = {
                BottomAppBar(
                    navController = navController,
                    user = user
                )
            },
            //snackbarHost = { SnackbarHost(snackbarHostState) },
            /*floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (!state.canSubmit) return@FloatingActionButton
                    onSubmit()
                    navController.navigateUp()
                }
            ) {
                Icon(Icons.Outlined.Check, "Add Travel")
            }
        },*/
        ) {contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "GPS e connessione ad internet sono necessari!\nAttivali per iniziare l'attività",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.size(15.dp))

                Row {

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Text("GPS")
                    }

                    Spacer(modifier = Modifier.size(15.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Text("Internet")
                    }
                }

                Spacer(modifier = Modifier.size(15.dp))

                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {
                        val trackingIntent = Intent(context, TrackingActivity::class.java).apply {
                            putExtra(TrackingActivity.EXTRA_PARAMETER, user.username)
                        }
                        ContextCompat.startActivity(context, trackingIntent, null)
                        activity.finish()
                    },
                    enabled = gpsChecker && internetConnChecker,
                    shape = CircleShape,
                    modifier= Modifier.size(70.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.DirectionsRun,
                        contentDescription = "Start running",
                        modifier = Modifier.scale(1.1F)
                    )
                }
            }


            /*contentPadding ->
                       Column(
                           verticalArrangement = Arrangement.spacedBy(12.dp),
                           horizontalAlignment = Alignment.CenterHorizontally,
                           modifier = Modifier
                               .padding(contentPadding)
                               .padding(12.dp)
                               .fillMaxSize()
                       ) {
                           OutlinedTextField(
                               value = state.destination,
                               onValueChange = actions::setDestination,
                               label = { Text("Destination") },
                               modifier = Modifier.fillMaxWidth(),
                               trailingIcon = {
                                   IconButton(onClick = ::requestLocation) {
                                       Icon(Icons.Outlined.MyLocation, "Current location")
                                   }
                               }
                           )
                           OutlinedTextField(
                               value = state.date,
                               onValueChange = actions::setDate,
                               label = { Text("Date") },
                               modifier = Modifier.fillMaxWidth()
                           )
                           OutlinedTextField(
                               value = state.description,
                               onValueChange = actions::setDescription,
                               label = { Text("Description") },
                               modifier = Modifier.fillMaxWidth()
                           )
                           Spacer(Modifier.size(24.dp))
                           Button(
                               onClick = ::takePicture,
                               contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                           ) {
                               Icon(
                                   Icons.Outlined.PhotoCamera,
                                   contentDescription = "Camera icon",
                                   modifier = Modifier.size(ButtonDefaults.IconSize)
                               )
                               Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                               Text("Take a picture")
                           }
                           Spacer(Modifier.size(8.dp))
                           ImageWithPlaceholder(state.imageUri, Size.Lg)
                       }
                   }*/
        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )

    /*if (state.showLocationDisabledAlert) {
        AlertDialog(
            title = { Text("Location disabled") },
            text = { Text("Location must be enabled to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationService.openLocationSettings()
                    actions.setShowLocationDisabledAlert(false)
                }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationDisabledAlert(false) }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { actions.setShowLocationDisabledAlert(false) }
        )
    }

    if (state.showLocationPermissionDeniedAlert) {
        AlertDialog(
            title = { Text("Location permission denied") },
            text = { Text("Location permission is required to get your current location in the app.") },
            confirmButton = {
                TextButton(onClick = {
                    locationPermission.launchPermissionRequest()
                    actions.setShowLocationPermissionDeniedAlert(false)
                }) {
                    Text("Grant")
                }
            },
            dismissButton = {
                TextButton(onClick = { actions.setShowLocationPermissionDeniedAlert(false) }) {
                    Text("Dismiss")
                }
            },
            onDismissRequest = { actions.setShowLocationPermissionDeniedAlert(false) }
        )
    }

    if (state.showLocationPermissionPermanentlyDeniedSnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                "Location permission is required.",
                "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                ctx.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", ctx.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            actions.setShowLocationPermissionPermanentlyDeniedSnackbar(false)
        }
    }

    if (state.showNoInternetConnectivitySnackbar) {
        LaunchedEffect(snackbarHostState) {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
            actions.setShowNoInternetConnectivitySnackbar(false)
        }
    }*/
}

fun checkGPS(context: Context): Boolean {
    val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun checkInternet(context: Context): Boolean {
    val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = mConnectivityManager.activeNetwork ?: return false
        val actNw = mConnectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        val nwInfo = mConnectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}