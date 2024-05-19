package com.example.outdoorromagna.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.GroupedTrack
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.repositories.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TracksState(val tracks: List<Track>)

data class GroupedTracksState(val tracks: List<GroupedTrack>)

class TracksDbViewModel(
    private val repository: TracksRepository
) : ViewModel() {
    val state = repository.tracks.map { TracksState(tracks = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TracksState(emptyList())
    )

    val groupedTracksState = repository.groupedTracks.map { GroupedTracksState(tracks = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = GroupedTracksState(emptyList())
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

    /*fun getGroupedTracks() = viewModelScope.launch {
            repository.getGroupedTracks()
    }*/

    fun getTracksInRange(startLat: Double, startLng: Double) =
        repository.getTracksInRange(startLat, startLng)

}