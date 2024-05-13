package com.example.outdoorromagna

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.outdoorromagna.data.database.OutdoorRomagnaDatabase
import com.example.outdoorromagna.data.remote.OSMDataSource
import com.example.outdoorromagna.data.repositories.PlacesRepository
import com.example.outdoorromagna.data.repositories.SettingsRepository
import com.example.outdoorromagna.data.repositories.ThemeRepository
import com.example.outdoorromagna.data.repositories.UsersRepository
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.screens.addtravel.AddTravelViewModel
import com.example.outdoorromagna.ui.screens.login.LoginViewModel
import com.example.outdoorromagna.ui.screens.home.HomeScreenViewModel
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import com.example.outdoorromagna.ui.screens.signin.SigninViewModel
import com.example.outdoorromagna.ui.theme.ThemeViewModel
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
            OutdoorRomagnaDatabase::class.java,
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

    single { ThemeRepository(get()) }

    single { UsersRepository(get<OutdoorRomagnaDatabase>().placesDAO()) }

    single {
        PlacesRepository(
            get<OutdoorRomagnaDatabase>().placesDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { HomeScreenViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { UsersViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { SigninViewModel() }
    //viewModel { PlacesViewModel(get()) }
}