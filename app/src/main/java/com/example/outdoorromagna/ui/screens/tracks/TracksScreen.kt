package com.example.outdoorromagna.ui.screens.tracks

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
import com.example.outdoorromagna.ui.screens.profile.ProfileActions
import com.example.outdoorromagna.ui.screens.profile.ProfileState
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
    Scaffold(
        topBar = { TopAppBar(navController, "OutdoorRomagna") },
        bottomBar = { BottomAppBar(navController, user) },
    ) { 
        contentPadding ->
        Column (
            modifier = Modifier.padding(all = 12.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Track screen",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }

    }
}
