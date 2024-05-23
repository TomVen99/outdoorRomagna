package com.example.outdoorromagna.ui.screens.addtrackdetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.outdoorromagna.data.database.Place
import com.example.outdoorromagna.utils.MapPresenter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddTrackDetailsState(
    val title: String = "",
    val description: String = "",
    val imageUri: Uri = Uri.EMPTY,

) {
    val canSubmit get() = title.isNotBlank() &&  description.isNotBlank()

    /*fun toPlace() = Place(
        name = title,
        description =  description,
        date = date,
        imageUri = imageUri.toString()
    )*/
}

interface AddTrackDetailsActions {
    fun setTitle(title: String)
    fun setDescription(description: String)
    fun setImageUri(imageUri: Uri)

    //fun setPresenter(presenter: MapPresenter)
/*
    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)*/
}

class AddTrackDetailsViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddTrackDetailsState())
    val state = _state.asStateFlow()

    val actions = object : AddTrackDetailsActions {
        override fun setTitle(title: String) =
            _state.update { it.copy(title = title) }

        override fun setDescription(description: String) =
            _state.update { it.copy(description = description) }

        override fun setImageUri(imageUri: Uri) =
            _state.update { it.copy(imageUri = imageUri) }

        /*override fun setPresenter(presenter: MapPresenter) {
            _state.update { it.copy(presenter = presenter) }
        }*/

        /*override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }*/
    }
}