package com.example.outdoorromagna.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.outdoorromagna.data.repositories.UsersRepository
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackScreen
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackViewModel
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
import com.example.outdoorromagna.ui.screens.trackdetails.TrackDetails
import com.example.outdoorromagna.ui.screens.tracks.TracksScreen
import com.example.outdoorromagna.ui.screens.tracks.TracksViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

sealed class OutdoorRomagnaRoute(
    val route: String,
    val title: String,
    var currentRoute: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : OutdoorRomagnaRoute("login", "Outdoor Romagna - Login", "")

    data object Signin : OutdoorRomagnaRoute("signin", "Outdoor Romagna - Signin", "")

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

    data object TrackDetails : OutdoorRomagnaRoute(
        "trackdetails/{userUsername}/{trackId}",
        "trackDetails",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("trackId") { type = NavType.IntType }
        )
    ) {
        fun buildRoute(userUsername: String, trackId: Int): String{
            setMyCurrentRoute("trackdetails/$userUsername/$trackId")
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
        "tracks/{userUsername}/{specificTrack}",
        "tracks",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType },
            navArgument("specificTrack") { type = NavType.BoolType }
        )
    ) {
        fun buildRoute(userUsername: String, specificTrack: Boolean) : String {
            setMyCurrentRoute("tracks/$userUsername/$specificTrack")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }

        /*fun buildRouteLatLng (userUsername: String,  latitude: String, longitude: String) : String {
            setMyCurrentRoute("tracks/$userUsername/$latitude/$longitude")
            return currentRoute
        }*/

    }

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

    data object AddTrack : OutdoorRomagnaRoute(
        "addtrack/{userUsername}",
        "AddTrack",
        "",
        listOf(
            navArgument("userUsername") { type = NavType.StringType }
        )
    ) {
        fun buildRoute(userUsername: String): String{
            setMyCurrentRoute("addtrack/$userUsername")
            return currentRoute
        }

        private fun setMyCurrentRoute (route : String) {
            currentRoute = route
        }
    }

    companion object {
        val routes = setOf(Login, Signin, Home, Settings, Profile, AddTrack)
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(DelicateCoroutinesApi::class)
@Composable
fun OutdoorRomagnaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    activity : Activity,
    intentRoute: String?,
) {

    val usersVm = koinViewModel<UsersViewModel>()
    val usersState by usersVm.state.collectAsStateWithLifecycle()
    var userDefault by remember{ mutableStateOf("null") }
    val tracksDbVm = koinViewModel<TracksDbViewModel>()
    val tracksDbState by tracksDbVm.state.collectAsStateWithLifecycle()
    val groupedTracksState by tracksDbVm.groupedTracksState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("isUserLogged", Context.MODE_PRIVATE)
    /*val edit = sharedPreferences.edit()
    edit.putBoolean("isUserLogged", false)
    edit.putString("username", "")
    edit.apply()*/
    var startDestination = ""
    /*if(intentRoute?.isNotEmpty() == true) {
        startDestination = OutdoorRomagnaRoute.Home.currentRoute
        sharedPreferences.getString("username", "")
            ?.let { OutdoorRomagnaRoute.Home.buildWithoutPosition(it) }
    } else {
        startDestination = OutdoorRomagnaRoute.Login.route
    }*/
    if (sharedPreferences.getBoolean("isUserLogged", false)) {
        val username = sharedPreferences.getString("username", "")
        Log.d("username", "username: " + username)
        if(username != null && username != "") {
            startDestination = OutdoorRomagnaRoute.Home.buildWithoutPosition(username)
        } else {
            startDestination = OutdoorRomagnaRoute.Login.route
        }
    } else {
        startDestination = OutdoorRomagnaRoute.Login.route
    }
    Log.d("TAG", "start destiantion: " + startDestination)

    NavHost(
        navController = navController,
        startDestination = startDestination,
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
                    onSubmit = {usersVm.login(state.username, state.password)},
                    navController,
                    usersVm,
                    sharedPreferences
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
                //usersVm.resetValues()
                val homeScreenVm = koinViewModel<HomeScreenViewModel>()
                val state by homeScreenVm.state.collectAsStateWithLifecycle()
                var userName =  backStackEntry.arguments?.getString("userUsername") ?: userDefault
                userName = if (userName == "null") userDefault else userName
                userDefault = userName
                Log.d("TAG", "username sharedPreferences " + sharedPreferences.getString("username", ""))
                Log.d("TAG", "lista username " + usersState.users)
                if(usersState.users.isNotEmpty()) {
                    val user = requireNotNull(usersState.users.find {
                        it.username == sharedPreferences.getString("username", "")//userName
                    })
                    Log.d("tag", "user.username " + user.username)
                    HomeScreen(
                        navController,
                        state,
                        homeScreenVm.actions,
                        user,
                        tracksDbVm,
                        tracksDbState,
                        groupedTracksState,
                        tracksDbState
                    )
                }
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
                    usersViewModel = usersVm,
                    sharedPreferences = sharedPreferences
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
                val isSpecificTrack = backStackEntry.arguments?.getBoolean("specificTrack") ?: false
                if (!isSpecificTrack)
                    tracksDbVm.resetSpecificTrackInRange()
                TracksScreen(
                    navController = navController,
                    user = user,
                    state = state,
                    actions = tracksVm.actions,
                    usersViewModel = usersVm,
                    tracksDbVm = tracksDbVm,
                    tracksDbState = tracksDbState
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
        with(OutdoorRomagnaRoute.AddTrack) {
            composable(route) {backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername")
                })
                val addTrackVm = koinViewModel<AddTrackViewModel>()
                val state by addTrackVm.state.collectAsStateWithLifecycle()
                AddTrackScreen(
                    state = state,
                    actions = addTrackVm.actions,
                    onSubmit = { /*tracksDbVm.addTrack(state.toPlace())*/ Log.d("TAG",  "add track to db")},
                    navController = navController,
                    user = user,
                    activity = activity
                )
            }
        }

        with(OutdoorRomagnaRoute.TrackDetails) {
            composable(route, arguments) { backStackEntry ->
                val user = requireNotNull(usersState.users.find {
                    it.username == backStackEntry.arguments?.getString("userUsername")
                })
                Log.d("io porco", tracksDbState.tracks.toString())
                val track = requireNotNull(tracksDbState.tracks.find {
                    it.id == backStackEntry.arguments?.getInt("trackId")
                })
                TrackDetails(
                    navController = navController,
                    user = user,
                    track = track)
            }
        }
    }
}

