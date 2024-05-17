package com.example.outdoorromagna.ui.screens.settings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(val username: String)

class SettingsViewModel @Inject constructor (
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val theme = settingsRepository.preferenceFlow

    fun saveTheme(theme:String) {
        Log.d("TAG", "dentro saveTheme")
        viewModelScope.launch {
            settingsRepository.saveToDataStore(theme)
        }
    }

    fun resetTheme() {
        viewModelScope.launch {
            settingsRepository.saveToDataStore("Light")
        }
    }
}
/*class SettingsViewModel @Inject constructor (
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState(""))
        private set
    val theme = settingsRepository.preferenceFlow

    fun saveTheme(theme:String) {
        viewModelScope.launch {
            settingsRepository.saveToDataStore(theme)
        }
    }
    /*fun setUsername(value: String) {
        state = SettingsState(value)
        viewModelScope.launch { settingsRepository.setUsername(value) }
    }

    init {
        viewModelScope.launch {
            state = SettingsState(settingsRepository.username.first())
        }
    }*/
}/*

class SettingsViewModel (
    private val repository: SettingsRepository_
) : ViewModel() {
    var state by mutableStateOf(SettingsState(""))
        private set

    fun setUsername(value: String) {
        state = SettingsState(value)
        viewModelScope.launch { repository.setUsername(value) }
    }

    init {
        viewModelScope.launch {
            state = SettingsState(repository.username.first())
        }
    }

    val theme = settingsRepository.preferenceFlow

    fun saveTheme(theme:String) {
        viewModelScope.launch {
            settingsRepository.saveToDataStore(theme)
        }
    }
}
*/