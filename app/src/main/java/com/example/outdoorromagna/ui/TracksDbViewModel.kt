package com.example.outdoorromagna.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.repositories.TracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TracksState(val tracks: List<Track>)

class TracksDbViewModel(
    private val repository: TracksRepository
) : ViewModel() {
    val state = repository.tracks.map { TracksState(tracks = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TracksState(emptyList())
    )

    fun addTrack(track: Track) = viewModelScope.launch {
        repository.upsert(track)
    }

    fun deleteTrack(track: Track) = viewModelScope.launch {
        repository.delete(track)
    }

    fun getAllTracks() = viewModelScope.launch {
        repository.getAllTracks()
    }
}