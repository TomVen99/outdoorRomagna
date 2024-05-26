package com.example.outdoorromagna.ui.screens.addtrack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbState
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.getMyDrawerState
import com.example.outdoorromagna.ui.screens.home.rememberPermission
import com.example.outdoorromagna.ui.screens.home.requestLocation
import com.example.outdoorromagna.utils.LocationService
import org.koin.compose.koinInject

@Composable
fun AddTrackScreen(
    navController: NavHostController,
    user: User,
    tracksDbState: TracksDbState
) {
    val context = LocalContext.current
    var gpsChecker by rememberSaveable { mutableStateOf(checkGPS(context)) }
    var internetConnChecker by rememberSaveable { mutableStateOf(checkInternet(context)) }
    var showGpsDialog by remember { mutableStateOf(false) }
    var showInternetDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val locationService = koinInject<LocationService>()
    val locationPermission = rememberPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    var isPermissionGranted by remember { mutableStateOf(false) }
    var hasClicked by remember { mutableStateOf(false) }
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(

                    navController = navController,
                    currentRoute = "Registra un percorso",
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
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                isPermissionGranted = locationPermission.status.isGranted
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        gpsChecker = checkGPS(context)
                        internetConnChecker = checkInternet(context)
                        hasClicked = true
                        if(!locationPermission.status.isGranted) {
                            requestLocation(locationPermission, locationService)
                        } else if(!gpsChecker) {
                            showGpsDialog = true
                        } else if(!internetConnChecker) {
                            showInternetDialog = true
                        } else {
                            hasClicked = false
                            navController.navigate(OutdoorRomagnaRoute.Tracking.buildRoute(user.username))
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.size(120.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                        contentDescription = "Start running",
                        modifier = Modifier.size(100.dp),
                    )
                }
            }

        if(
        locationPermission.status.isGranted &&
        gpsChecker &&
        internetConnChecker &&
        hasClicked
        ) {
            hasClicked = false
            navController.navigate(OutdoorRomagnaRoute.Tracking.buildRoute(user.username))
        }
        if(showGpsDialog)
            ShowDialog(
                title = "Gps richiesto",
                text = "Per poter registrare un percorso è necessario che il GPS sia attivo",
                onDismiss = {showGpsDialog = false},
                onConfirm = { requestToActivateGps(context) }
            )
        if(showInternetDialog)
            ShowDialog(
                title = "Internet richiesto",
                text = "Per poter registrare un percorso è necessario che internet sia attivo",
                onDismiss = {showInternetDialog = false},
                onConfirm = { requestToActivateInternet(context) }
            )
        if(locationPermission.status.isDenied && hasClicked)
            ShowLocationPermissionDenied(
                context = context,
                text = "Il permesso per la posizione è stato negato. Non è possibile avviare un percorso.",
                onDismiss = {
                    hasClicked = false
                }
            )
        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController,
        tracksDbState,
    )
}

private fun requestToActivateGps(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

private fun requestToActivateInternet(context: Context) {
    val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}



@Composable
fun ShowDialog(
    title: String,
    text: String,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    onConfirm()
                }
            ) {
                Text("OK", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}

@Composable
fun ShowLocationPermissionDenied(
    context: Context,
    text: String,
    onDismiss: () -> Unit = {},
) {
    val openAppSettingsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Permesso Negato") },
        text = { Text(text) },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text(
                    text = "OK",
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
                openAppSettingsLauncher.launch(
                    Intent().apply {
                        action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }) {
                Text(
                    text = "Impostazioni",
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    )
}

fun checkGPS(context: Context): Boolean {
    val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun checkInternet(context: Context): Boolean {
    val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val nw = mConnectivityManager.activeNetwork ?: return false
    val actNw = mConnectivityManager.getNetworkCapabilities(nw) ?: return false
    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}
