package com.example.outdoorromagna.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Marker::class, Favorite::class], version = 2)
abstract class OutdoorRomagnaDatabase : RoomDatabase() {
    abstract fun placesDAO(): PlacesDAO
}