package com.example.outdoorromagna

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.outdoorromagna.data.database.TravelDiaryDatabase
import com.example.outdoorromagna.data.remote.OSMDataSource
import com.example.outdoorromagna.data.repositories.PlacesRepository
import com.example.outdoorromagna.data.repositories.SettingsRepository
import com.example.outdoorromagna.ui.PlacesViewModel
import com.example.outdoorromagna.ui.screens.addtravel.AddTravelViewModel
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import com.example.outdoorromagna.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            TravelDiaryDatabase::class.java,
            "travel-diary"
        )
            // Sconsigliato per progetti seri! Lo usiamo solo qui per semplicità
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { OSMDataSource(get()) }

    single { LocationService(get()) }

    single { SettingsRepository(get()) }

    single {
        PlacesRepository(
            get<TravelDiaryDatabase>().placesDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { AddTravelViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { PlacesViewModel(get()) }
}
