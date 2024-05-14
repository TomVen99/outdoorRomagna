package com.example.outdoorromagna.ui.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileState(
    val username: String = "",
    val password: String = ""
) {
    val canSubmitUser get() = username.isNotBlank()
    val canSubmitPassword get() = password.isNotBlank()

}

interface ProfileActions {
    fun setUsername(title: String)
    fun setPassword(date: String)
}

class ProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val actions = object : ProfileActions {
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }
    }
}