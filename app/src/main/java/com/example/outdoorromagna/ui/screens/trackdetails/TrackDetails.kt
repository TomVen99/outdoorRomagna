package com.example.outdoorromagna.ui.screens.trackdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.composables.BottomAppBar
import com.example.outdoorromagna.ui.composables.TopAppBar
import com.example.outdoorromagna.ui.composables.getMyDrawerState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline

@Composable
fun TrackDetails(
    navController: NavHostController,
    user: User,
    track: Track
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                currentRoute = "Dettagli Percorso",
                showSearch = false,
                drawerState = getMyDrawerState(),
                scope = scope,
                showFilter = false
            )
        },
        bottomBar = { BottomAppBar(navController, user) },
    ) { contentPadding ->
        Card(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(
                text = track.name,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = track.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, bottom = 10.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                fontFamily = FontFamily.Default
            )
            if (track.imageUri != null) {
                val painter = rememberAsyncImagePainter(model = track.imageUri)
                Image(
                    painter = painter,
                    contentDescription = "Track Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 16.dp)
                )
            }
            Text(
                text = "Città: ${track.city}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )
            val minutes = if (track.duration > 1) "minuti" else "minuto"
            Text(
                text = "Durata: ${track.duration}  $minutes",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, top = 10.dp, bottom = 10.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = androidx.compose.ui.text.style.TextAlign.Start
            )
            Card(
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    cameraPositionState = CameraPositionState(
                        CameraPosition(LatLng(track.startLat, track.startLng), 14f, 0f, 0f)
                    )
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(track.startLat, track.startLng)),

                    )

                    Polyline(
                        points = track.trackPositions,
                        color = Color.Blue
                    )
                }

            }
        }
    }
}