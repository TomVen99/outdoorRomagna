package com.example.outdoorromagna.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.example.outdoorromagna.R
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.databinding.ActivityMapsBinding
import com.example.outdoorromagna.ui.screens.addtrack.ActivitiesViewModel
import com.example.outdoorromagna.utils.MapPresenter
import com.example.outdoorromagna.utils.Ui
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrackingActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val EXTRA_PARAMETER = "extra_parameter"
    }

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val isStarted: MutableLiveData<Boolean> = MutableLiveData(false)
    private val presenter = MapPresenter(this, isStarted)
    private val activitiesViewModel: ActivitiesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        /*setTheme(R.style.Theme_Mobile_project)*/
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra(EXTRA_PARAMETER)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnStartStop.setOnClickListener {
            if (binding.btnStartStop.text == getString(R.string.start_label)) {
                isStarted.value = true
                startTracking()
                binding.btnStartStop.setText(R.string.stop_label)
            } else {
                val context = this
                val sharedPreferences: SharedPreferences =
                    context.getSharedPreferences("usernameLoggedPref", Context.MODE_PRIVATE)

                isStarted.value = false
                Log.d("TAG", "prima stopTracking")
                if(username?.isNotEmpty() == true) {
                    stopTracking(context, activitiesViewModel, username/*sharedPreferences*/)
                }
                Log.d("TAG", "dopo stopTracking")
                binding.btnStartStop.setText(R.string.start_label)
            }
        }

        presenter.onViewCreated()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        presenter.ui.observe(this) { ui ->
            updateUi(ui)
        }

        presenter.onMapLoaded()
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun startTracking() {
        binding.container.txtPace.text = ""
        binding.container.txtDistance.text = ""
        binding.container.txtTime.base = SystemClock.elapsedRealtime()
        binding.container.txtTime.start()
        map.clear()

        presenter.startTracking()
    }

    private fun stopTracking(
        context: Context,
        activitiesViewModel: ActivitiesViewModel,
        //sharedPreferences: SharedPreferences
        username: String
    ) {
        binding.container.txtTime.stop()
        val elapsedTime = (SystemClock.elapsedRealtime() - binding.container.txtTime.base) / 1000
        presenter.stopTracking(context, username,/*sharedPreferences,*/ activitiesViewModel, elapsedTime)
    }

    @SuppressLint("MissingPermission")
    private fun updateUi(ui: Ui) {
        if (ui.currentLocation != null && ui.currentLocation != map.cameraPosition.target) {
            map.isMyLocationEnabled = true
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(ui.currentLocation, 14f))
        }
        binding.container.txtDistance.text = ui.formattedDistance
        binding.container.txtPace.text = ui.formattedSteps

        drawRoute(ui.userPath)
    }

    private fun drawRoute(locations: List<LatLng>) {
        val polylineOptions = PolylineOptions()

        map.clear()

        val points = polylineOptions.points
        points.addAll(locations)

        map.addPolyline(polylineOptions)
    }
}