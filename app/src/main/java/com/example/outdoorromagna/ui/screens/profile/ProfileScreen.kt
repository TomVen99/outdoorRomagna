package com.example.outdoorromagna.ui.screens.profile

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.rememberPermission
import com.example.outdoorromagna.ui.screens.sideBarMenu.SideBarMenu
import com.example.outdoorromagna.ui.screens.sideBarMenu.getMyDrawerState
import com.example.outdoorromagna.ui.screens.sideBarMenu.items
import com.example.outdoorromagna.ui.theme.DarkBrown
import com.example.outdoorromagna.ui.theme.LightBrown
import com.example.outdoorromagna.utils.createImageFile
import com.example.outdoorromagna.utils.rememberCameraLauncher
import com.example.outdoorromagna.utils.saveImage
import kotlinx.coroutines.launch
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    user: User,
    onModify: (User) -> Unit,
    //state: ProfileState,
    //actions: ProfileActions,
    usersViewModel : UsersViewModel
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val cameraLauncher = rememberCameraLauncher()

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    @Composable
    fun setProfileImage() {
        val imageModifier = Modifier
            .size(200.dp)
            .border(
                BorderStroke(2.dp, Color.Black),
                CircleShape
            )
            .clip(CircleShape)
        Log.d("TAG", "PRIMA imagePath not empty")
        if(cameraLauncher.capturedImageUri.path?.isNotEmpty() == true){
            Log.d("TAG", "imagePath not empty")
            AsyncImage(
                model = ImageRequest.Builder(ctx)
                    .data(cameraLauncher.capturedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "image taken",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
            user.username.let { usersViewModel.updateProfileImg(it, saveImage(ctx.applicationContext.contentResolver, cameraLauncher.capturedImageUri)) }
            cameraLauncher.capturedImageUri = Uri.EMPTY
        } else if (user.urlProfilePicture?.length == 0){
            Log.d("TAG", "urlProfilePicture = null")
            Image(
                painter = painterResource(id = R.drawable.baseline_android_24),
                contentDescription = "image placeholder",
                modifier = imageModifier.background(Color.LightGray),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onSecondaryContainer)
            )
        } else {
            Log.d("TAG", "urlProfilePicture not null " + user.urlProfilePicture.toString())
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(Uri.parse(user.urlProfilePicture))
                    .crossfade(true)
                    .build(),
                contentDescription = "profile img",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }
    }




    val myScaffold: @Composable () -> Unit = {

        Scaffold(
            topBar = {
                TopAppBar(
                    navController = navController,
                    currentRoute = OutdoorRomagnaRoute.Profile.title,
                    drawerState = getMyDrawerState(),
                    scope = scope,
                    showLogout = true
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
                Log.d("TAG", "dentro myscaffold")
                Text(
                    text = "Nome e Cognome",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.size(15.dp))
                setProfileImage()
                Spacer(modifier = Modifier.size(15.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    onClick = {
                        takePicture()
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
        }
    }

    SideBarMenu(
        myScaffold = myScaffold,
        navController
    )
}


