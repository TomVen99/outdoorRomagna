package com.example.outdoorromagna.ui.screens.filterBar

import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.outdoorromagna.ui.screens.tracks.TracksActions

enum class FilterOption(val title: String) {
    YOUR_TRACKS("Tuoi percorsi"),
    ALL_TRACKS("Tutti i percorsi"),
    FAVORITES("Percorsi preferiti")

}
@Composable
fun FilterBar(/*onQueryChanged: (String) -> Unit, */actions: TracksActions) {
    /*var text by remember { mutableStateOf("") }*/
    Log.d("TAG", "Dentro filter bar")
    /*var selectedFilter by remember { mutableStateOf("Tutti") }*/
    val scrollState = rememberScrollState()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    var currentFilter by remember { mutableStateOf(FilterOption.ALL_TRACKS) }
    NavigationBar(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .horizontalScroll(scrollState)
            .padding(vertical = 1.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        FilterOption.entries.forEachIndexed { index, text ->
            NavigationBarItem(
                modifier = Modifier.padding(horizontal = 2.dp),
                onClick = {
                    Log.d("TAG", "cliccato " + text)
                    selectedItemIndex = index
                    val filteredItems = when (FilterOption.entries[selectedItemIndex]) {
                        FilterOption.YOUR_TRACKS -> Log.d("TAG", FilterOption.YOUR_TRACKS.title)
                        FilterOption.ALL_TRACKS -> Log.d("TAG", FilterOption.ALL_TRACKS.title)
                        FilterOption.FAVORITES -> Log.d("TAG", FilterOption.FAVORITES.title)
                    }
                    //actions.setShowFilter(false)
                },
                icon = { /*TODO*/ },
                selected = index == selectedItemIndex,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                label = { Text(text = FilterOption.entries[index].title) }
            )
        }
    }
}