package com.example.outdoorromagna.ui.screens.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class SigninState(val username: String)

class SigninViewModel (
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SigninState(""))
        private set

    fun setUsername(value: String) {
        state = SigninState(value)
        viewModelScope.launch { repository.setUsername(value) }
    }

    init {
        viewModelScope.launch {
            state = SigninState(repository.username.first())
        }
    }
}
