package com.example.outdoorromagna.ui.screens.profile

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.outdoorromagna.utils.rememberCameraLauncher


@Composable
fun ProfileScreen(
    navController: NavHostController,
    user: User,
    onModify: (User) -> Unit,
    state: ProfileState,
    actions: ProfileActions,
    viewModel : UsersViewModel
) {/*
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (user.urlProfilePicture == "default.png") {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .padding(16.dp)
                    .size(200.dp)  // Adjust the size to be about 1/4th and manageable
                    .clip(CircleShape)  // Apply a circular clip to the image
            )
        } else {
            AsyncImage(
                model = user.urlProfilePicture.toUri(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(200.dp)  // Adjust the size to be about 1/4th and manageable
                    .clip(CircleShape)  // Apply a circular clip to the image
            )
        }

        Button(
            onClick = { takePicture() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Fai una foto")
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.username,
                onValueChange = actions::setUsername,
                label = { Text("Username") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (!state.canSubmitUser) return@Button
                    //onModify(User(user.id, state.username, user.password, user.urlProfilePicture, user.salt))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)
            ) {
                Text(text = "Cambia Username")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            OutlinedTextField(
                value = state.password,
                onValueChange = actions::setPassword,
                label = { Text("Password") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (!state.canSubmitPassword) return@Button
                    val salt = viewModel.generateSalt()
                    val password = viewModel.hashPassword(state.password, salt)
                    //onModify(User(user.id, user.username, password, user.urlProfilePicture, salt))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)
            ) {
                Text(text = "Cambia Password")
            }
        }


    }

    // Update the user's profile picture if a new picture is taken
    if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
        //onModify(User(user.id, user.username, user.password, cameraLauncher.capturedImageUri.toString(), user.salt))
    }
    //DropMenu(user = user, navController = navController)*/


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

    Scaffold (
        topBar = { TopAppBar(navController, "OutdoorRomagna") },
        bottomBar = { BottomAppBar(navController, user) },
    ){
        contentPadding -> //perchè da errore???????????????????????????
        Column (
            modifier = Modifier.padding(all = 12.dp).fillMaxSize(),
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
                    takePicture()
                }
                ,
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

    // Update the user's profile picture if a new picture is taken
    if (cameraLauncher.capturedImageUri.path?.isNotEmpty() == true) {
        onModify(User(user.id, user.username, user.password, cameraLauncher.capturedImageUri.toString(), user.salt))
    }
}
