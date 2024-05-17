package com.example.outdoorromagna.ui.screens.tracks

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.rememberPermission
import com.example.outdoorromagna.ui.screens.profile.ProfileActions
import com.example.outdoorromagna.ui.screens.profile.ProfileState
import com.example.outdoorromagna.ui.screens.sideBarMenu.SideBarMenu
import com.example.outdoorromagna.ui.screens.sideBarMenu.getMyDrawerState
import com.example.outdoorromagna.utils.rememberCameraLauncher


@Composable
fun TracksScreen(
    navController: NavHostController,
    user: User,
    //onModify: (User) -> Unit,
    state: TracksState,
    actions: TracksActions,
    viewModel : UsersViewModel
) {
    /*val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)*/
    val scope = rememberCoroutineScope()
    val myScaffold: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = "OutdoorRomagna",
                    showSearch = true,
                    drawerState = getMyDrawerState(),
                    scope = scope
                )
            },
            bottomBar = { BottomAppBar(navController, user) },
        ) { contentPadding ->
            Column(

                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var isFavorite by remember { mutableStateOf(false) }
                val items = listOf("Pippo", "pluto", "paperino")
                items.forEach() { text ->
                    ListItem(
                        headlineContent = { Text(text= text) },
                        supportingContent = {
                            Text("Secondary text that is long and perhaps goes onto another line")
                        },
                        trailingContent = {
                            IconButton(onClick = {
                                isFavorite = !isFavorite
                                Log.d("TAG", "addFavorite")
                            })
                            {
                                Icon(imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Add to favorites")
                            }
                        },
                    )
                }
                /*Text(
                    text = "Track screen",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )*/
            }

        }
    }
    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )
}
