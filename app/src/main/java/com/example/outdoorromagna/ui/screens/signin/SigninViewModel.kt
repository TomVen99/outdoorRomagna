package com.example.outdoorromagna.ui.screens.signin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SigninState(
    val username: String = "",
    val password: String = "",
    val salt: ByteArray = ByteArray(800)
) {
    val canSubmit get() = username.isNotBlank() && password.isNotBlank()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SigninState

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

interface SigninActions {
    fun setUsername(username: String)
    fun setPassword(password: String)
    fun setSalt(byteArray: ByteArray)
}

class SigninViewModel : ViewModel() {
    private val _state = MutableStateFlow(SigninState())
    val state = _state.asStateFlow()

    val actions = object : SigninActions {
        override fun setUsername(username: String) =
            _state.update { it.copy(username = username) }

        override fun setPassword(password: String) =
            _state.update { it.copy(password = password) }

        override fun setSalt(byteArray: ByteArray) {
            _state.update { it.copy(salt = byteArray) }
        }
    }
}





/*data class SigninState(val username: String)

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
*/