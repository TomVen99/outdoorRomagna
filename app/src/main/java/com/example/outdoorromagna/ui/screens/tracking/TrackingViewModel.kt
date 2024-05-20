package com.example.outdoorromagna.ui.screens.tracking

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TrackingState(
    val username: String = "",
    val password: String = ""
) {
    val canSubmitUser get() = username.isNotBlank()
    val canSubmitPassword get() = password.isNotBlank()

}

interface TrackingActions {
    fun setUsername(title: String)
    fun setPassword(date: String)
}

class TrackingViewModel : ViewModel() {
    private val _state = MutableStateFlow(TrackingState())
    val state = _state.asStateFlow()

    val actions = object : TrackingActions {
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }
    }
}