package com.example.outdoorromagna

import android.content.Context
import android.os.Bundle
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
import com.example.outdoorromagna.ui.theme.OutdoorRomagnaTheme
import com.example.outdoorromagna.utils.LocationService
import org.koin.android.ext.android.get

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//test
class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val settingsViewModel: SettingsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.resetTheme()
        locationService = get<LocationService>()

        setContent {
            val intentRoute = intent.getStringExtra("route")



                /*val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.state.collectAsStateWithLifecycle()

            OutdoorRomagnaTheme(
                darkTheme = when (themeState.theme) {
                    Theme.Light -> false
                    Theme.Dark -> true
                    Theme.System -> isSystemInDarkTheme()
                }
            ) {*/
                //val settingsViewModel = koinViewModel<SettingsViewModel>()
                val theme by settingsViewModel.theme.collectAsState(initial = "")
                OutdoorRomagnaTheme(darkTheme = theme == "Dark") {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val snackbarHostState = remember { SnackbarHostState() }
                        var showLocationDisabledAlert by remember { mutableStateOf(false) }
                        var showPermissionDeniedAlert by remember { mutableStateOf(false) }
                        var showPermissionPermanentlyDeniedSnackbar by remember {
                            mutableStateOf(
                                false
                            )
                        }

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

                        Scaffold(
                            /*topBar = { TopAppBar(navController, currentRoute) },
                        bottomBar = { BottomAppBar(navController, currentRoute) },*/
                            //snackbarHost = { SnackbarHost(snackbarHostState) }
                        ) { contentPadding ->
                            OutdoorRomagnaNavGraph(
                                navController,
                                modifier = Modifier.padding(contentPadding),
                                activity = this,
                                intentRoute
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

    override fun onDestroy() {
        /*Log.d("TAG", "sono qui")
        settingsViewModel.saveTheme("Light")*/
        super.onDestroy()
    }



}
