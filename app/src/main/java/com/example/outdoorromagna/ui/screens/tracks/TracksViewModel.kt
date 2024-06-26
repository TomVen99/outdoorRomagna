package com.example.outdoorromagna.ui.screens.tracks

import androidx.lifecycle.ViewModel
import com.example.outdoorromagna.ui.composables.FilterOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TracksState(
    /*val username: String = "",
    val password: String = "",*/
    val showFilterBar: Boolean = false,
    val filter: FilterOption = FilterOption.ALL_TRACKS,
) {
    val isShowFilterEnabled get() = showFilterBar
}

interface TracksActions {
    /*fun setUsername(title: String)
    fun setPassword(date: String)*/
    fun setShowFilter(show: Boolean)
    fun setFilter(filter: FilterOption)
}

class TracksViewModel : ViewModel() {
    private val _state = MutableStateFlow(TracksState())
    val state = _state.asStateFlow()

    val actions = object : TracksActions {
        /*override fun setUsername(title: String) =
            _state.update { it.copy(username = title) }

        override fun setPassword(date: String) =
            _state.update { it.copy(password = date) }*/

        override fun setShowFilter(show: Boolean) =
            _state.update { it.copy(showFilterBar = show) }

        override fun setFilter(filter: FilterOption) {
            _state.update { it.copy(filter = filter) }
        }

        /*override fun isShowFilterEnabled(): Boolean {
            return isShowFilterStateEnabled()
        }*/

    }
}

/*data class TrackItem (
    val title: String,
    var isFavorite: Boolean,
    val shortDescription: String?
)*/