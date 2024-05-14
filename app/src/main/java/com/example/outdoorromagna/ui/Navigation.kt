package com.example.outdoorromagna.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.outdoorromagna.ui.screens.addtravel.AddTravelScreen
import com.example.outdoorromagna.ui.screens.addtravel.AddTravelViewModel
import com.example.outdoorromagna.ui.screens.home.HomeScreen
import com.example.outdoorromagna.ui.screens.home.HomeScreenViewModel
import com.example.outdoorromagna.ui.screens.login.Login
import com.example.outdoorromagna.ui.screens.login.LoginViewModel
import com.example.outdoorromagna.ui.screens.profile.ProfileScreen
import com.example.outdoorromagna.ui.screens.profile.ProfileViewModel
import com.example.outdoorromagna.ui.screens.settings.SettingsScreen
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import com.example.outdoorromagna.ui.screens.signin.SigninScreen
import com.example.outdoorromagna.ui.screens.signin.SigninViewModel
import com.example.outdoorromagna.ui.screens.traveldetails.TravelDetailsScreen
import org.koin.androidx.compose.koinViewModel

sealed class OutdoorRomagnaRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : OutdoorRomagnaRoute("login", "Outdoor Romagna - Login")

    data object Signin : OutdoorRomagnaRoute("signin", "Outdoor Romagna - Signin")

    /*data object Home : OutdoorRomagnaRoute(
        "home",
        "Outdoor Romagna - Home"
    )*/
    data object Home : OutdoorRomagnaRoute(
        "home/{userUsername}/{latitude}/{longitude}",
        "homePage",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )
    ) {
        fun buildRoute(userUsername: String, latitude: Float?, longitude: Float?)
                = "home/$userUsername/$latitude/$longitude"
        fun buildWithoutPosition (userUsername: String) = "home/$userUsername/0/0"
    }
    data object TravelDetails : OutdoorRomagnaRoute(
        "travels/{travelId}",
        "Travel Details",
        listOf(navArgument("travelId") { type = NavType.StringType })
    ) {
        fun buildRoute(travelId: String) = "home/$travelId"
    }
    data object AddTravel : OutdoorRomagnaRoute("travels/add", "Add Travel")
    data object Settings : OutdoorRomagnaRoute("settings", "Settings")

    data object Profile : OutdoorRomagnaRoute("profile", "Profile") {
        fun buildRoute(userId: String) = "profile/$userId"
    }

    companion object {
        val routes = setOf(Login, Signin, Home, TravelDetails, AddTravel, Settings, Profile)
    }
}

@Composable
fun OutdoorRomagnaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val usersVm = koinViewModel<UsersViewModel>()
    val usersState by usersVm.state.collectAsStateWithLifecycle()
    var userDefault by remember{ mutableStateOf("null") }

    NavHost(
        navController = navController,
        startDestination = OutdoorRomagnaRoute.Login.route,
        modifier = modifier
    ) {
        with(OutdoorRomagnaRoute.Login) {
            composable(route) {
                usersVm.resetValues()
                val loginVm = koinViewModel<LoginViewModel>()
                val state by loginVm.state.collectAsStateWithLifecycle()
                Login(
                    state,
                    actions = loginVm.actions,
                    onSubmit = {usersVm.login(state.toUser())},
                    navController,
                    usersVm
                )
            }
        }
        with(OutdoorRomagnaRoute.Signin) {
            composable(route) {
                usersVm.resetValues()
                val signinVm = koinViewModel<SigninViewModel>()
                val signinState by signinVm.state.collectAsStateWithLifecycle()
                SigninScreen(
                    state = signinState,
                    actions = signinVm.actions,
                    onSubmit = { usersVm.addUser(it) },
                    navController = navController,
                    usersVm)
            }
        }
        with(OutdoorRomagnaRoute.Home) {
            composable(route, arguments) {backStackEntry ->
                usersVm.resetValues()
                val homeScreenVm = koinViewModel<HomeScreenViewModel>()
                val state by homeScreenVm.state.collectAsStateWithLifecycle()
                Log.d("TAG", backStackEntry.arguments.toString())
                Log.d("TAG", backStackEntry.arguments?.getString("userUsername").toString())
                var userName =  backStackEntry.arguments?.getString("userUsername") ?: userDefault
                userName = if (userName == "null") userDefault else userName
                userDefault = userName
                Log.d("TAG", "username scritto da me " + userName)
                val user = requireNotNull(usersState.users.find {
                    //Log.d("TAG", "username default " + userName)
                    it.username == userName
                })
                Log.d("tag", "user.username " + user.username)
                HomeScreen(
                    navController,
                    state,
                    homeScreenVm.actions,
                    user
                )
            }
        }
        with(OutdoorRomagnaRoute.Profile) {
            composable(route, arguments) {backStackEntry ->
                val profileVm = koinViewModel<ProfileViewModel>()
                val state by profileVm.state.collectAsStateWithLifecycle()
                val user = requireNotNull(usersState.users.find {
                    it.id == backStackEntry.arguments?.getString("userId")!!.toInt()
                })
                ProfileScreen(
                    navController = navController,
                    user = user,
                    //onModify = usersVm::addUserWithoutControl,
                    state = state,
                    actions = profileVm.actions,
                    viewModel = usersVm
                )
            }
        }/*
        with(OutdoorRomagnaRoute.TravelDetails) {
            composable(route, arguments) { backStackEntry ->
                val place = requireNotNull(placesState.places.find {
                    it.id == backStackEntry.arguments?.getString("travelId")?.toInt()
                })
                TravelDetailsScreen(place)
            }
        }
        with(OutdoorRomagnaRoute.AddTravel) {
            composable(route) {
                val addTravelVm = koinViewModel<AddTravelViewModel>()
                val state by addTravelVm.state.collectAsStateWithLifecycle()
                AddTravelScreen(
                    state = state,
                    actions = addTravelVm.actions,
                    onSubmit = { placesVm.addPlace(state.toPlace()) },
                    navController = navController
                )
            }
        }
        with(OutdoorRomagnaRoute.Settings) {
            composable(route) {
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(settingsVm.state, settingsVm::setUsername)
            }
        }*/
    }
}

