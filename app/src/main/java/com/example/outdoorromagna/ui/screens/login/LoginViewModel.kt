package com.example.outdoorromagna.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class LoginState(val username: String)

class LoginViewModel (
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState(""))
        private set

    fun setUsername(value: String) {
        state = LoginState(value)
        viewModelScope.launch { repository.setUsername(value) }
    }

    init {
        viewModelScope.launch {
            state = LoginState(repository.username.first())
        }
    }
}
