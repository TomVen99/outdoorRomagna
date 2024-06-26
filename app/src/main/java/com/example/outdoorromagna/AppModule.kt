package com.example.outdoorromagna

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.outdoorromagna.data.database.OutdoorRomagnaDatabase
import com.example.outdoorromagna.data.remote.OSMDataSource
import com.example.outdoorromagna.data.repositories.FavoritesRepository
import com.example.outdoorromagna.data.repositories.SettingsRepository
import com.example.outdoorromagna.data.repositories.ThemeRepository
import com.example.outdoorromagna.data.repositories.TracksRepository
import com.example.outdoorromagna.data.repositories.UsersRepository
import com.example.outdoorromagna.ui.FavouritesDbViewModel
import com.example.outdoorromagna.ui.TracksDbViewModel
import com.example.outdoorromagna.ui.UsersViewModel
import com.example.outdoorromagna.ui.screens.addtrack.AddTrackViewModel
import com.example.outdoorromagna.ui.screens.addtrackdetails.AddTrackDetailsViewModel
import com.example.outdoorromagna.ui.screens.login.LoginViewModel
import com.example.outdoorromagna.ui.screens.home.HomeScreenViewModel
import com.example.outdoorromagna.ui.screens.profile.ProfileViewModel
import com.example.outdoorromagna.ui.screens.settings.SettingsViewModel
import com.example.outdoorromagna.ui.screens.signin.SigninViewModel
import com.example.outdoorromagna.ui.screens.tracking.TrackingViewModel
import com.example.outdoorromagna.ui.screens.tracks.TracksViewModel
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
            "outdoorRomagna"
        )
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

    single { FavoritesRepository(get<OutdoorRomagnaDatabase>().favoritesDAO()) }

    single { UsersRepository(get<OutdoorRomagnaDatabase>().usersDAO()) }

    single {
        TracksRepository(
            get<OutdoorRomagnaDatabase>().tracksDAO(),
            get<Context>().applicationContext.contentResolver
        )
    }

    viewModel { HomeScreenViewModel() }

    viewModel { TracksViewModel() }

    viewModel { SettingsViewModel(get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { UsersViewModel(get()) }

    viewModel { TracksDbViewModel(get()) }

    viewModel { FavouritesDbViewModel(get()) }

    viewModel { LoginViewModel() }

    viewModel { SigninViewModel() }

    viewModel { ProfileViewModel() }

    viewModel { AddTrackViewModel() }

    viewModel { AddTrackDetailsViewModel() }

    viewModel { TrackingViewModel() }

}
