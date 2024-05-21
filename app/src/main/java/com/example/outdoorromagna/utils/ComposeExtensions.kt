package com.example.outdoorromagna.utils

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@Composable
fun <T> MutableLiveData<T>.observeAsState(initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }
    DisposableEffect(this) {
        val observer = Observer<T> { t ->
            state.value = t
        }
        observeForever(observer)
        onDispose { removeObserver(observer) }
    }
    Log.d("TAG", "USATO FUNZIONE")
    return state
}
