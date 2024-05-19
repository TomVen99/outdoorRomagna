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
import com.example.outdoorromagna.ui.screens.tracks.TracksScreen
import com.example.outdoorromagna.ui.screens.tracks.TracksViewModel
import org.koin.androidx.compose.koinViewModel

sealed class OutdoorRomagnaRoute(
    val route: String,
    val title: String,
    var currentRoute: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : OutdoorRomagnaRoute("login", "Outdoor Romagna - Login", "")

    data object Signin : OutdoorRomagnaRoute("signin", "Outdoor Romagna - Signin", "")

    /*data object Home : OutdoorRomagnaRoute(
        "home",
        "Outdoor Romagna - Home"
    )*/
    data object Home : OutdoorRomagnaRoute(
        "home/{userUsername}/{latitude}/{longitude}",
        "homePage",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )
    ) {
        fun buildRoute(userUsername: String, latitude: Float?, longitude: Float?): String{
            setMyCurrentRoute("home/$userUsername/$latitude/$longitude")
            return currentRoute
        }

        fun buildWithoutPosition (userUsername: String) : String {
            setMyCurrentRoute("home/$userUsername/0/0")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object Profile : OutdoorRomagnaRoute(
        "profile/{userUsername}",
        "Profile",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("profile/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    data object Tracks : OutdoorRomagnaRoute(
        "tracks/{userUsername}",
        "tracks",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    /*,
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("latitude") { type = NavType.FloatType },
            navArgument("longitude") { type = NavType.FloatType }
        )*/
    ) {
        fun buildRoute(userUsername: String) : String {
            setMyCurrentRoute("tracks/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
        /*
        fun buildRoute(userUsername: String, latitude: Float?, longitude: Float?)
                = "home/$userUsername/$latitude/$longitude"
        fun buildWithoutPosition (userUsername: String) = "home/$userUsername/0/0"
        */
    }
    data object TravelDetails : OutdoorRomagnaRoute(
        "travels/{travelId}",
        "Travel Details",
        "",
        listOf(navArgument("travelId") { type = NavType.StringType })
    ) {
        fun buildRoute(travelId: String) = "home/$travelId"
    }
    data object AddTravel : OutdoorRomagnaRoute("travels/add", "Add Travel", "")
    //data object Settings : OutdoorRomagnaRoute("settings", "Settings", "")
    data object Settings : OutdoorRomagnaRoute(
        "settings/{userUsername}",
        "Settings",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("settings/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
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

    val tracksDbVm = koinViewModel<TracksDbViewModel>()
    val tracksDbState by tracksDbVm.state.collectAsStateWithLifecycle()

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
                    user,
                    tracksDbVm,
                    tracksDbState
                )
            }
        }
        with(OutdoorRomagnaRoute.Profile) {
            composable(route, arguments) {backStackEntry ->
                val profileVm = koinViewModel<ProfileViewModel>()
                val state by profileVm.state.collectAsStateWithLifecycle()
                /*val user =  backStackEntry.arguments?.getString("userUsername") ?: userDefault
                user = if (user == "null") userDefault else user
                userDefault = user*/
                val user = requireNotNull(usersState.users.find {
                    //it.id == backStackEntry.arguments?.getString("userId")!!.toInt()
                    it.username == backStackEntry.arguments?.getString("userUsername")
                })
                ProfileScreen(
                    navController = navController,
                    user = user,
                    onModify = usersVm::addUserWithoutControl,
                    /*state = state,
                    actions = profileVm.actions,*/
                    usersViewModel = usersVm
                )
            }
        }
        with(OutdoorRomagnaRoute.Tracks) {
            composable(route, arguments) {backStackEntry ->
                val tracksVm = koinViewModel<TracksViewModel>()
                val state by tracksVm.state.collectAsStateWithLifecycle()
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername")
                })

                TracksScreen(
                    navController = navController,
                    user = user,
                    //onModify = usersVm::addUserWithoutControl,
                    state = state,
                    actions = tracksVm.actions,
                    viewModel = usersVm
                )
            }
        }
        with(OutdoorRomagnaRoute.Settings) {
            composable(route) {backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername")
                })
                val settingsVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(
                    settingsVm,
                    navController,
                    user = user
                )
            }
        }
        /*
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
        */
    }
}

