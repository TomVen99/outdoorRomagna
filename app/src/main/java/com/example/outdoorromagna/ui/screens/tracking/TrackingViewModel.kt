package com.example.outdoorromagna.ui.screens.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TrackingState(
    /*val username: String = "",
    val duration: Long = 0,
    val distance: Int = 0,
    var isTracking: Boolean = false,
    val steps: Int = 0,*/
    val showLocationPermissionDenied: Boolean = false
)
interface TrackingActions {
    /*fun setUsername(title: String)
    fun setTracking(isTracking: Boolean)
    fun isTracking(): MutableLiveData<Boolean?>
    fun setDuration(duration: Long)
    fun setDistance(distance: Int)//Forse da cambiare
    fun setSteps(steps: Int)*/

    fun setShowLocationPermissionDenied(show: Boolean)
}

class TrackingViewModel : ViewModel() {

    private val _isTracking: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    val isTracking: LiveData<Boolean?> = _isTracking
    private val _duration = MutableLiveData<Long>()
    val duration: LiveData<Long> = _duration
    private val _distance = MutableLiveData<Int>()
    val distance: LiveData<Int> = _distance
    private val _steps = MutableLiveData<Int>()
    val steps: LiveData<Int> = _steps
    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()


    val actions = object : TrackingActions {

        override fun setShowLocationPermissionDenied(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDenied = show) }
        /*
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setTracking(isTracking: Boolean) {
            _state.update { it.copy(isTracking = isTracking) }
        }

        override fun isTracking(): MutableLiveData<Boolean?> {
            return _isTracking
        }

        override fun setDuration(duration: Long) {
            _state.update { it.copy(duration = duration) }
        }

        override fun setDistance(distance: Int) {
            _state.update { it.copy(distance = distance) }
        }

        override fun setSteps(steps: Int) {
            _state.update { it.copy(steps = steps) }
        }
*/
    }
}