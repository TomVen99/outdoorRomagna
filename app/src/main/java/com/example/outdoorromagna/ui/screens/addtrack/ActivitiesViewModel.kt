package com.example.outdoorromagna.ui.screens.addtrack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.outdoorromagna.data.database.Activity
import com.example.outdoorromagna.data.repositories.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ActivitiesViewModel (
    private val repository: ActivityRepository
): ViewModel() {

    val allActivities = repository.allActivities
    private val activityLiveData = MutableLiveData<List<Activity>>()

    fun insertActivity(activity: Activity) = viewModelScope.launch {
        repository.insertActivity(activity)
    }

    fun deleteActivity(activity: Activity) = viewModelScope.launch {
        repository.deleteActivity(activity)
    }

    fun updateActivity(activity: Activity) = viewModelScope.launch {
        repository.updateActivity(activity)
    }

    fun deleteAllActivities() = viewModelScope.launch {
        repository.deleteAllActivities()
    }

    fun updateActivityName(activityId: String, name: String) = viewModelScope.launch {
        repository.updateActivityName(activityId, name)
    }

    fun getActivitiesFromUsername(username: String): Flow<List<Activity>> {
        return repository.getActivitiesFromUser(username)
    }

    fun getFavouriteActivitiesFromUser(username: String): Flow<List<Activity>> {
        return repository.getFavouriteActivitiesFromUser(username)
    }

    fun updateActivityFavourite(activityId: String, favourite: Boolean) = viewModelScope.launch {
        repository.updateActivityFavourite(activityId, favourite)
    }

    private var _activitySelected: Activity? = null
    val activitySelected
        get() = _activitySelected

    fun selectActivity(activity: Activity) {
        _activitySelected = activity
    }
}
