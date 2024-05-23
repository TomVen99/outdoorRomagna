package com.example.outdoorromagna.ui.screens.addtrackdetails

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackState

@Composable
fun AddTrackDetailsScreen(
    navController: NavController,
    addTrackDetailsVm: AddTrackDetailsViewModel,
    addTrackDetailsState: AddTrackDetailsState,
    addTrackDetailsActions: AddTrackDetailsActions,
    user: User,
    addTrackState: AddTrackState,
    tracksDbVm: TracksDbViewModel
){
    val ctx = LocalContext.current
    Scaffold { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(contentPadding)
                .padding(12.dp)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = addTrackDetailsState.title,
                onValueChange = addTrackDetailsActions::setTitle,
                label = { Text("Titolo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = addTrackDetailsState.description,
                onValueChange = addTrackDetailsActions::setDescription,
                label = { Text("Descrizione") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.size(24.dp))
            Button(
                onClick = {
                    Log.d("TAG", addTrackState.startLat.toString())
                    Log.d("TAG", addTrackState.startLng.toString())
                    if (!addTrackDetailsState.canSubmit) {
                        Toast.makeText(ctx, "Inserisci i valori", Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(OutdoorRomagnaRoute.AddTrack.currentRoute)
                        tracksDbVm.addTrack(
                            Track(
                                city = addTrackState.city,
                                duration = addTrackState.duration,
                                trackPositions = addTrackState.trackPositions,
                                startLat = addTrackState.startLat,
                                startLng = addTrackState.startLng,
                                description = addTrackDetailsState.description,
                                name = addTrackDetailsState.title,
                                imageUri = null
                            )
                        )
                    }
                },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    Icons.Outlined.DoneOutline,
                    contentDescription = "save icon",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Salva")
            }
        }
    }
}