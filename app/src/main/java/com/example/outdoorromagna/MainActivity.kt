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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

//test
class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val settingsViewModel: SettingsViewModel by viewModel()
    /*private val usersVm: UsersViewModel by viewModel()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.resetTheme()
        locationService = get<LocationService>()

        setContent {
            val intentRoute = intent.getStringExtra("route")
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
                            intentRoute,
                            //usersVm = usersVm
                        )
                    }
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
        //locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        //locationService.resumeLocationRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
    }



}
