package com.example.outdoorromagna

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
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsViewModel.resetTheme()
        locationService = get<LocationService>()

        setContent {
            val theme by settingsViewModel.theme.collectAsState(initial = "")
            OutdoorRomagnaTheme(darkTheme = theme == "Dark") {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    OutdoorRomagnaRoute.routes.find {
                        it.route == backStackEntry?.destination?.route
                    } ?: OutdoorRomagnaRoute.Login
                    Scaffold{ contentPadding ->
                        OutdoorRomagnaNavGraph(
                            navController,
                            modifier = Modifier.padding(contentPadding),
                        )
                    }
                }
            }
        }
    }
    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
