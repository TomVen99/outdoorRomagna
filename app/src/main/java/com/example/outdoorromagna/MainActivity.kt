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
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.theme.OutdoorRomagnaTheme
import com.example.outdoorromagna.utils.LocationService
import org.koin.android.ext.android.get

import android.util.Log

//test
class MainActivity : ComponentActivity() {
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationService = get<LocationService>()

        setContent {
            OutdoorRomagnaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        Log.d("TAG", "Questo è un messaggio di debug")
                        derivedStateOf {
                            OutdoorRomagnaRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: OutdoorRomagnaRoute.Login
                        }
                    }

                    /*Scaffold(
                        topBar = { TopAppBar(navController, currentRoute) },
                        bottomBar = { BottomAppBar(navController, currentRoute )}
                    ) { contentPadding ->
                        OutdoorRomagnaNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }*/
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        locationService.pauseLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        locationService.resumeLocationRequest()
    }
}
