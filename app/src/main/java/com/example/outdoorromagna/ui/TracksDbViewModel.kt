package com.example.outdoorromagna.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.GroupedTrack
import com.example.outdoorromagna.data.database.Track
import com.example.outdoorromagna.data.repositories.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TracksDbState(val tracks: List<Track>)

data class GroupedTracksState(val tracks: List<GroupedTrack>)

class TracksDbViewModel(
    private val repository: TracksRepository
) : ViewModel() {
    val state = repository.tracks.map { TracksDbState(tracks = it) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TracksDbState(emptyList())
    )
    private val _specificTracksList = MutableLiveData<List<Track>?>()
    val specificTracksList: LiveData<List<Track>?> = _specificTracksList

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
        _specificTracksList.value = null
        repository.getAllTracks()

    }
    fun getUserTracks(id: Int) = viewModelScope.launch {
        val tracks = repository.getUserTracks(id)
        _specificTracksList.value = tracks
    }
    fun getFavoriteTracks(id: Int) = viewModelScope.launch {
        repository.getFavoriteTracks(id)
    }


    /*fun getGroupedTracks() = viewModelScope.launch {
            repository.getGroupedTracks()
    }*/

    fun getTracksInRange(startLat: Double, startLng: Double) {
        viewModelScope.launch {
            val tracks = repository.getTracksInRange(startLat, startLng)
            _specificTracksList.value = tracks
        }
    }

    fun resetSpecificTrackInRange() {
        _specificTracksList.value = null
    }


}