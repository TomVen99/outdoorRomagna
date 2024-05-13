package com.example.outdoorromagna.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.User
import com.example.outdoorromagna.data.repositories.UsersRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.SecureRandom

data class UsersState(val users: List<User>)

class UsersViewModel(
    private val repository: UsersRepository
) : ViewModel() {
    private val _addUserResult = MutableLiveData<Boolean?>()
    val addUserResult: LiveData<Boolean?> = _addUserResult
    private val _addUserLog = MutableLiveData<String?>()
    val addUserLog: LiveData<String?> = _addUserLog

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult
    private val _loginLog = MutableLiveData<String?>()
    val loginLog: LiveData<String?> = _loginLog

    val state = repository.users.map { UsersState(users = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UsersState(emptyList())
    )

    fun addUser(user: User) {
        viewModelScope.launch {
            val userMatch = repository.getUser(user.username).firstOrNull()
            if (userMatch == null) {
                repository.upsert(user)
                _addUserResult.value = true
            } else {
                _addUserResult.value = false
                _addUserLog.value = "errore: username già esistente"
            }

        }
    }

    fun addUserWithoutControl(user: User) {
        viewModelScope.launch {
            repository.upsert(user)
        }
    }

    fun login(user:User) {
        viewModelScope.launch {
            val userMatch = repository.getUser(user.username).firstOrNull()
            if (userMatch != null ) {
                _loginResult.value = userMatch.password == hashPassword(user.password, userMatch.salt)
                _loginLog.value = if (_loginResult.value!!) "" else "errore: Password sbagliata"
            } else {
                _loginResult.value = false
                _loginLog.value = "errore: Username non esiste"
            }

        }
    }

    fun resetValues() {
        _addUserResult.value = null
        _loginResult.value = null
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.delete(user)
    }

    fun hashPassword(password: String, salt: ByteArray): String {
        val bytes = password.toByteArray() + salt
        val digest = MessageDigest.getInstance("SHA-256").digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    fun generateSalt(length: Int = 16): ByteArray {
        val salt = ByteArray(length)
        val random = SecureRandom()
        random.nextBytes(salt)
        return salt
    }
}
