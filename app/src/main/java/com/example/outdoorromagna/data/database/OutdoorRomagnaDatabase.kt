package com.example.outdoorromagna.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, Track::class, Activity::class], version = 13)
@TypeConverters(LatLngListConverter::class)
abstract class OutdoorRomagnaDatabase : RoomDatabase() {
    abstract fun usersDAO(): UsersDAO

    abstract fun tracksDAO(): TracksDAO

}