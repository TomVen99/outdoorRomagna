package com.example.outdoorromagna.ui.screens.sideBarMenu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.outdoorromagna.R
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.theme.DarkBrown
import com.example.outdoorromagna.ui.theme.LightBrown
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val route: OutdoorRomagnaRoute
)

var drawerState: DrawerState = DrawerState(DrawerValue.Closed)

fun getMyDrawerState() : DrawerState {
    return drawerState
}

val items = listOf(
    NavigationItem(
        title = "Mappa",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map,
        route = OutdoorRomagnaRoute.Home
    ),
    NavigationItem(
        title = "Percorsi",
        selectedIcon = Icons.Filled.Directions,
        unselectedIcon = Icons.Outlined.Directions,
        badgeCount = 45,
        route = OutdoorRomagnaRoute.Tracks
    ),
    NavigationItem(
        title = "Profilo",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = OutdoorRomagnaRoute.Profile
    ),
    NavigationItem(
        title = "Impostazioni",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = OutdoorRomagnaRoute.Settings
    ),
)

@Composable
fun SideBarMenu (
    myScaffold: @Composable () -> Unit,
    navController: NavHostController
){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        var selectedItemIndex by rememberSaveable {
            mutableStateOf(0)
        }
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet (
                    drawerContainerColor = LightBrown
                ){
                    Image(
                        painter = painterResource(id = R.drawable.outdoorromagna),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        alignment = Alignment.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Utente")
                    Spacer(modifier = Modifier.height(16.dp))
                    items.forEachIndexed { index, item ->
                        NavigationDrawerItem(
                            modifier = Modifier
                                .alpha(0.5.toFloat())
                                .padding(horizontal = 5.dp, vertical = 5.dp)
                                .border(
                                    shape = RoundedCornerShape(25.dp),
                                    width = 1.dp,
                                    color = DarkBrown
                                ), // Imposta lo spessore e il colore del bordo

                            label = {
                                Text(text = item.title)
                            },
                            selected = index == selectedItemIndex,
                            onClick = {
                                navController.navigate(item.route.currentRoute)//.navigate(OutdoorRomagnaRoute.Home.buildWithoutPosition("a"))
                                selectedItemIndex = index
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            badge = {
                                item.badgeCount?.let {
                                    Text(text = item.badgeCount.toString())
                                }
                            },
                        )

                    }
                }
            },
            drawerState = drawerState
        ) {
            Log.d("TAG", "prima di myscaffold")
            myScaffold()
            /*Scaffold(
                topBar = {
                    TopAppBar(
                        navController = navController,
                        currentRoute = OutdoorRomagnaRoute.Profile.title,
                        drawerState = drawerState,
                        scope = scope
                    )
                },
                bottomBar = {
                    BottomAppBar(
                        navController = navController,
                        user = user
                    )
                }
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nome e Cognome",
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    Spacer(modifier = Modifier.size(15.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        onClick = {
                            //takePicture()
                        },
                    ) {
                        Icon(
                            Icons.Filled.PhotoCamera,
                            contentDescription = "Camera icon",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Scatta foto")
                    }

                    Spacer(modifier = Modifier.size(15.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "account image"//stringResource(id = 0)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            /*text = if (user?.firstName?.isNotEmpty() == true
                            && user?.lastName?.isNotEmpty() == true) {
                            user.firstName + " " + user.lastName
                        } else "Nome Cognome",*/
                            text = user.username,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    Spacer(modifier = Modifier.size(15.dp))

                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Icon(
                            Icons.Filled.Mail,
                            contentDescription = "mail"//stringResource(id = 1)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "email",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }


                }
            }*/
        }
    }
}