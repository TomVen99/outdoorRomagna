package com.example.outdoorromagna.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.Activity
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.screens.addtrack.ActivitiesViewModel
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackState
import com.example.outdoorromagna.ui.screens.tracking.Ui
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.Locale
import kotlin.math.round

class MapPresenter(private val context: Context,
                   private val registry: ActivityResultRegistry,
                   private val isStarted: MutableLiveData<Boolean>,
                   private val tracksDbVm: TracksDbViewModel) {

    lateinit var ui: MutableLiveData<Ui>
    private lateinit var googleMap: GoogleMap
    private lateinit var locationProvider: LocationProvider
    private lateinit var stepCounter: StepCounter
    private lateinit var permissionsManager: PermissionsManager


    val elapsedTime = MutableLiveData<Long>()

    private var startTime = 0L
    private var myLocations = mutableListOf<LatLng>()
    fun setGoogleMap(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    fun mySetUi(ui: MutableLiveData<Ui>) {
        this.ui = ui
    }
    fun onMapLoaded(context: Context) {
        locationProvider = LocationProvider(context, isStarted)
        stepCounter = StepCounter(context)
        permissionsManager = PermissionsManager(registry, locationProvider, stepCounter)
        permissionsManager.requestUserLocation()
    }

    private fun stopTracking() {
        isStarted.value = false
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()
    }

    fun onViewCreated(lifecycleOwner: LifecycleOwner) {
        locationProvider.liveLocations.observe(lifecycleOwner) { locations ->
            val current = ui.value
            ui.value = current?.copy(userPath = locations)
            Log.d("TAG", "drawRoute locations "+ locations)
            myLocations = Collections.unmodifiableList(locations)
            drawRoute(locations)
        }

        locationProvider.liveLocation.observe(lifecycleOwner) { currentLocation ->
            val current = ui.value
            ui.value = current?.copy(currentLocation = currentLocation)
            updateMapLocation(currentLocation)
        }

        locationProvider.liveDistance.observe(lifecycleOwner) { distance ->
            val current = ui.value
            val formattedDistance = context.getString(R.string.distance_value, distance)
            ui.value = current?.copy(formattedDistance = formattedDistance, distance = distance)
        }

        stepCounter.liveSteps.observe(lifecycleOwner) { steps ->
            val current = ui.value
            val formattedSteps = context.getString(R.string.steps_value, steps)
            Log.d("TAG", "steps " + steps)
            ui.value = current?.copy(formattedSteps = formattedSteps, steps = steps)
        }

    }

    private fun updateMapLocation(location: LatLng?) {
        location?.let {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()

        googleMap.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        googleMap.addPolyline(polylineOptions)
        /*Log.d("TAG", locations.toString())*/
    }

    fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            Log.d("TAG", "abilitato")
        }
    }

    fun startTracking() {
        startTime = SystemClock.elapsedRealtime()
        isStarted.value = true
        locationProvider.trackUser()
        permissionsManager.requestActivityRecognition()
        updateElapsedTime()
    }

    private fun updateElapsedTime() {
        if (isStarted.value == true) {
            val currentTime = SystemClock.elapsedRealtime()
            val elapsed = currentTime - startTime
            elapsedTime.value = elapsed / 1000 // Convert to seconds
            Handler(Looper.getMainLooper()).postDelayed({ updateElapsedTime() }, 1000)
        }
    }

    fun stopTracking(
        context: Context,
        //sharedPreferences: SharedPreferences,
        username: String,
        activitiesViewModel: ActivitiesViewModel,
        addTrackState: AddTrackState,
    ): Boolean {

        Log.d("TAG", "myLocations stopTracking " + myLocations.toString())
        val result = updateTrackState(myLocations, addTrackState)
        locationProvider.stopTracking()
        stepCounter.unloadStepCounter()
        return result
    }

    private fun updateTrackState(
        /*username: String,
        activitiesViewModel: ActivitiesViewModel,
        elapsedTime: Long*/
        myLocations: List<LatLng>,
        addTrackState: AddTrackState,
    ): Boolean {

        Log.d("TAG", "myLocations addTrack " + myLocations.toString())
        val city = getCityFromLatLng(context, myLocations[0])
        if(!city.isNullOrEmpty()) {
            Log.d("CITY", city)
            addTrackState.city = city
            addTrackState.duration = /*elapsedTime*/ 10.5
            addTrackState.trackPositions = myLocations
            addTrackState.startLat = myLocations[0].latitude
            addTrackState.startLng = myLocations[0].longitude
            /*addTrackState.imageUri = "URI"*/
            /*tracksDbVm.addTrack(
                Track(
                    city = city,
                    name = title,
                    description = description,
                    duration = /*elapsedTime*/ 10.5,
                    trackPositions = myLocations,
                    startLat = myLocations[0].latitude,
                    startLng = myLocations[0].longitude,
                    imageUri = null
                )
            )*/
            return true
        } else {
            Log.d("TAG","Errore CITTA")
            return false
        }
    }

    private fun getCityFromLatLng(context: Context, latLng: LatLng): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        var city: String? = null

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                city = addresses[0].locality
            }
        } catch (e: IOException) {
            Log.e("Geocoder", "Error getting city from LatLng", e)
        }

        return city
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
        val steps = ui.value?.steps

        if (distance != 0) {
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
                    steps = steps,
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



