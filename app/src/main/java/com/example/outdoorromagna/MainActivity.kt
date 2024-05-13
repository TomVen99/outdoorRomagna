package com.example.outdoorromagna

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.outdoorromagna.ui.OutdoorRomagnaNavGraph
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.theme.OutdoorRomagnaTheme
import com.example.outdoorromagna.utils.LocationService
import org.koin.android.ext.android.get

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.camera.utils.PermissionStatus
import com.example.camera.utils.rememberPermission
import com.example.outdoorromagna.ui.theme.Theme
import com.example.outdoorromagna.ui.theme.ThemeViewModel
import org.koin.androidx.compose.koinViewModel

//test
class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationService = get<LocationService>()

        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            OutdoorRomagnaTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    var showLocationDisabledAlert by remember { mutableStateOf(false) }
                    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
                    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }

                    /*val locationPermission = rememberPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) { status ->
                        when (status) {
                            PermissionStatus.Granted -> {
                                val res = locationService.requestCurrentLocation()
                                showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                            }

                            PermissionStatus.Denied ->
                                showPermissionDeniedAlert = true

                            PermissionStatus.PermanentlyDenied ->
                                showPermissionPermanentlyDeniedSnackbar = true

                            PermissionStatus.Unknown -> {}
                        }
                    }

                    fun requestLocation() {
                        if (locationPermission.status.isGranted) {
                            val res = locationService.requestCurrentLocation()
                            showLocationDisabledAlert = res == StartMonitoringResult.GPSDisabled
                        } else {
                            locationPermission.launchPermissionRequest()

                        }
                    }*/

                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            OutdoorRomagnaRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: OutdoorRomagnaRoute.Login
                        }
                    }
                    val ctx = LocalContext.current

                    Scaffold(
                        topBar = { TopAppBar(navController, currentRoute) },
                        bottomBar = { BottomAppBar(navController, currentRoute) },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { contentPadding ->
                        OutdoorRomagnaNavGraph(
                            navController,
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                    /* { contentPadding ->
                        position.setLatitude(locationService.coordinates?.latitude?.toFloat() ?: position.latitude.value!!)
                        position.setLongitude(locationService.coordinates?.longitude?.toFloat() ?: position.longitude.value!!)
                        TravelDiaryNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding),
                            position = position,
                            onPosition = { requestLocation() },
                            themeState = themeState,
                            onThemeSelected = themeViewModel::changeTheme,
                        )
                    }*/
                    /*
                    if (showLocationDisabledAlert) {
                        AlertDialog(
                            title = { Text("Location disabled") },
                            text = { Text("Location must be enabled to get your current location in the app.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    locationService.openLocationSettings()
                                    showLocationDisabledAlert = false
                                }) {
                                    Text("Enable")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showLocationDisabledAlert = false }) {
                                    Text("Dismiss")
                                }
                            },
                            onDismissRequest = { showLocationDisabledAlert = false }
                        )
                    }

                    if (showPermissionDeniedAlert) {
                        AlertDialog(
                            title = { Text("Location permission denied") },
                            text = { Text("Location permission is required to get your current location in the app.") },
                            confirmButton = {
                                TextButton(onClick = {
                                    locationPermission.launchPermissionRequest()
                                    showPermissionDeniedAlert = false
                                }) {
                                    Text("Grant")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showPermissionDeniedAlert = false }) {
                                    Text("Dismiss")
                                }
                            },
                            onDismissRequest = { showPermissionDeniedAlert = false }
                        )
                    }

                    if (showPermissionPermanentlyDeniedSnackbar) {
                        LaunchedEffect(snackbarHostState) {
                            val res = snackbarHostState.showSnackbar(
                                "Location permission is required.",
                                "Go to Settings",
                                duration = SnackbarDuration.Long
                            )
                            if (res == SnackbarResult.ActionPerformed) {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", ctx.packageName, null)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                if (intent.resolveActivity(ctx.packageManager) != null) {
                                    ctx.startActivity(intent)
                                }
                            }
                            showPermissionPermanentlyDeniedSnackbar = false
                        }
                    }*/
                }
            }
        }
    }

            /*OutdoorRomagnaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Immagine sopra il modulo di accesso
                        Image(
                            painter = painterResource(id = R.drawable.outdoorromagna), // Sostituisci "R.drawable.your_image_resource" con il tuo ID di risorsa immagine
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .padding(bottom = 16.dp) // Spazio inferiore tra l'immagine e il modulo di accesso
                        )

                        // Modulo di accesso
                        val navController = rememberNavController()
                        val backStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute by remember {
                            Log.d("TAG", "Main activity")
                            derivedStateOf {
                                OutdoorRomagnaRoute.routes.find {
                                    it.route == backStackEntry?.destination?.route
                                } ?: OutdoorRomagnaRoute.Login
                            }
                        }

                        var username by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }

                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Username") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    Log.d("TAG", username + "" + password)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Login")
                            }
                        }
                    }*/

                    /*
                    Scaffold(
                        topBar = { TopAppBar(navController, currentRoute) },
                        bottomBar = { BottomAppBar(navController, currentRoute )}
                    ) { contentPadding ->
                        OutdoorRomagnaNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }*/

    override fun onPause() {
        super.onPause()
        //locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        //locationService.resumeLocationRequest()
    }
}
