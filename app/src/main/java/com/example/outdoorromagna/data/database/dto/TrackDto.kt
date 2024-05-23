package com.example.outdoorromagna.data.database.dto

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

data class TrackDto (
    val name: String?,
    val description: String?,
    val city: String,
    val startLat: Double,
    val startLng: Double,
    val trackPositions: List<LatLng>,
    val imageUri: String?,
    val duration: Long,
)