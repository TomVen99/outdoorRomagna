package com.example.outdoorromagna.ui.screens.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeScreenState(
    val destination: String = "",
    val date: String = "",
    val description: String = "",
    val imageUri: Uri = Uri.EMPTY,

    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
    val showNoInternetConnectivitySnackbar: Boolean = false
) {
    val canSubmit get() = destination.isNotBlank() && date.isNotBlank() && description.isNotBlank()

    /*fun toPlace() = Place(
        name = destination,
        description =  description,
        date = date,
        imageUri = imageUri.toString()
    )*/
}

interface MapActions {
    fun setDestination(title: String)
    fun setDate(date: String)
    fun setDescription(description: String)
    fun setImageUri(imageUri: Uri)

    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)
}

class HomeScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    val actions = object : MapActions {
        override fun setDestination(title: String) =
            _state.update { it.copy(destination = title) }

        override fun setDate(date: String) =
            _state.update { it.copy(date = date) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }

        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }
    }
}