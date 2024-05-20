package com.example.outdoorromagna.ui.screens.tracking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.TracksDbState
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun TrackingScreen(
    navController: NavHostController,
    state: TrackingState,
    actions: TrackingActions,
    user : User,
    /*tracksDbVm: TracksDbViewModel,
    tracksDbState: TracksDbState,*/
)
{
    var isTracking by remember { mutableStateOf(false) }
    val buttonText = if (isTracking) stringResource(id = R.string.stop_label) else stringResource(id = R.string.start_label)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_color))
    ) {
        // Include layout for indicators
        // Assuming you have a separate composable for this
        Indicators()

        val cameraPositionState = rememberCameraPositionState()

        Box(modifier = Modifier.weight(1f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            )
        }

        Button(
            onClick = { isTracking = !isTracking },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = buttonText, color = Color.Black)
        }
    }
}

@Composable
fun Indicators() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(colorResource(id = R.color.background_color))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IndicatorRow(
            iconResId = R.drawable.ic_pace,
            description = stringResource(id = R.string.steps),
            label = stringResource(id = R.string.steps),
            value = "3.4 m/s"
        )
        IndicatorRow(
            iconResId = R.drawable.ic_time,
            description = stringResource(id = R.string.elapsed_time_label),
            label = stringResource(id = R.string.elapsed_time_label),
            value = "45 minutes"
        )
        IndicatorRow(
            iconResId = R.drawable.ic_distance,
            description = stringResource(id = R.string.distance_label),
            label = stringResource(id = R.string.distance_label),
            value = "12 km"
        )
    }
}

@Composable
fun IndicatorRow(
    iconResId: Int,
    description: String,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = description,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            //style = MaterialTheme.typography.body2,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            //style = MaterialTheme.typography.body1
        )
    }
}