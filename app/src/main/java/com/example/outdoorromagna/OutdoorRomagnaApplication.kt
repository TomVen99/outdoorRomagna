package com.example.outdoorromagna

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OutdoorRomagnaApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@OutdoorRomagnaApplication)
            modules(appModule)
        }
    }
}
