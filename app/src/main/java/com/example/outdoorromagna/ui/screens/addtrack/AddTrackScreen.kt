package com.example.outdoorromagna.ui.screens.addtrack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.SideBarMenu
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.getMyDrawerState
import com.example.outdoorromagna.ui.screens.addtrackdetails.AddTrackDetailsViewModel

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

    // UI
    val gpsChecker by rememberSaveable { mutableStateOf(checkGPS(context)) }
    val internetConnChecker by rememberSaveable { mutableStateOf(checkInternet(context)) }
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
        ) { contentPadding ->
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
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        Log.d("TAG", "Prima di vado in tracking")
                        navController.navigate(OutdoorRomagnaRoute.Tracking.buildRoute(user.username))
                        Log.d("TAG", "Vado in tracking")
                        /*val trackingIntent = Intent(context, TrackingActivity::class.java).apply {
                            putExtra(TrackingActivity.EXTRA_PARAMETER, user.username)
                        }
                        ContextCompat.startActivity(context, trackingIntent, null)*/

                    },
                    enabled = gpsChecker && internetConnChecker,
                    shape = CircleShape,
                    modifier = Modifier.size(70.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.DirectionsRun,
                        contentDescription = "Start running",
                        modifier = Modifier.scale(1.1F)
                    )
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
private fun ShowAlertDialog(navController: NavHostController) {
    AlertDialog(
        onDismissRequest = { /* Gestisci la chiusura dell'alert */ },
        title = { Text("Configurazione errata") },
        text = { Text("È necessario che localizzazione e internet siano attivi.") },
        confirmButton = {
            Button(onClick = { navController.navigate(OutdoorRomagnaRoute.AddTrack.currentRoute) }) {
                Text(
                    text = "OK",
                    color = MaterialTheme.colorScheme.onSecondary
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
