package com.example.outdoorromagna.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.PendingIntentCompat.getActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import com.example.outdoorromagna.MainActivity
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.Activity
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.ui.OutdoorRomagnaRoute
import com.example.outdoorromagna.ui.screens.addtrack.ActivitiesViewModel
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

class MapPresenter(private val activity: AppCompatActivity, private val isStarted: MutableLiveData<Boolean>) {

    val ui = MutableLiveData(Ui.EMPTY)
    private val locationProvider = LocationProvider(activity, isStarted)
    private val stepCounter = StepCounter(activity)
    private val permissionsManager = PermissionsManager(activity, locationProvider, stepCounter)

    fun onViewCreated() {
        locationProvider.liveLocations.observe(activity) { locations ->
            val current = ui.value
            ui.value = current?.copy(userPath = locations)
        }

        locationProvider.liveLocation.observe(activity) { currentLocation ->
            val current = ui.value
            ui.value = current?.copy(currentLocation = currentLocation)
        }

        locationProvider.liveDistance.observe(activity) { distance ->
            val current = ui.value
            val formattedDistance = activity.getString(R.string.distance_value, distance)
            ui.value = current?.copy(formattedDistance = formattedDistance, distance = distance)
        }

        stepCounter.liveSteps.observe(activity) { steps ->
            val current = ui.value
            ui.value = current?.copy(formattedSteps = "$steps")
        }
    }

    fun onMapLoaded() {
        permissionsManager.requestUserLocation()
    }

    fun startTracking() {
        locationProvider.trackUser()
        permissionsManager.requestActivityRecognition()
    }

    fun stopTracking(
        context: Context,
        //sharedPreferences: SharedPreferences,
        username: String,
        activitiesViewModel: ActivitiesViewModel,
        elapsedTime: Long
    ) {
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()

        val result = insertNewActivity(context, username,/*sharedPreferences,*/ activitiesViewModel, elapsedTime)
        val homeIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("route", OutdoorRomagnaRoute.Home.currentRoute)
        }
        if (!result) {
            Toast.makeText(context, "Errore nell'inserimento dell'attività", Toast.LENGTH_LONG).show()
        }
        activity.finish()
    }

    /**
     * returns true if the activity is saved correctly,
     * false otherwise
     */
    private fun insertNewActivity(
        context: Context,
        //sharedPreferences: SharedPreferences,
        username: String,
        activitiesViewModel: ActivitiesViewModel,
        elapsedTime: Long
    ): Boolean {
        val time = elapsedTime.toDouble()
        val distance = ui.value?.distance

        if (distance != null && distance != 0) {
            val speed = (round(distance!! / time * 36) / 10)
            val pace = (round(time / distance * 166.667) / 10)
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            //sharedPreferences.getString(context.getString(R.string.username_shared_pref), "")?.let {
                activitiesViewModel.insertActivity(
                    Activity(
                        userCreatorUsername = username,//it,
                        name = "Nuova attività",
                        description = "Inserisci una descrizione",
                        totalTime = elapsedTime,
                        distance = distance,
                        speed = speed,
                        pace = pace,
                        steps = 10,//ui.value?.formattedSteps?.toInt(),
                        onFoot = null,
                        favourite = false,
                        date = LocalDateTime.now().format(dateFormatter)
                    )
                )
            //}
            return true
        }
        return false
    }
}

data class Ui(
    val formattedSteps: String,
    val distance: Int,
    val formattedDistance: String,
    val currentLocation: LatLng?,
    val userPath: List<LatLng>
) {

    companion object {

        val EMPTY = Ui(
            formattedSteps = "",
            distance = 0,
            formattedDistance = "",
            currentLocation = null,
            userPath = emptyList()
        )
    }
}

