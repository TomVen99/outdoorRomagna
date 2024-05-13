package com.example.outdoorromagna.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
/*
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
}*/
data class LoginState(
    val username: String = "",
    val password: String = "",
    val salt: ByteArray = ByteArray(800)
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()

    fun toUser() = User(
        username = username,
        password = password,
        urlProfilePicture = "default.png",
        salt = salt
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginState

        if (username != other.username) return false
        if (password != other.password) return false
        return salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

interface LoginActions {
    fun setUsername(title: String)
    fun setPassword(date: String)
    fun setSalt(salt : ByteArray)
}

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = object : LoginActions {
        override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }

        override fun setSalt(salt: ByteArray) {
            _state.update { it.copy(salt = salt) }
        }
    }
}
