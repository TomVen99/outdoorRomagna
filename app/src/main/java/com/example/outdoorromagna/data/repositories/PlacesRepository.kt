package com.example.outdoorromagna.data.repositories

import android.content.ContentResolver
import android.net.Uri
import com.example.camera.utils.saveImageToStorage
import com.example.outdoorromagna.data.database.Place
import com.example.outdoorromagna.data.database.UsersDAO

class PlacesRepository(
    private val usersDAO: UsersDAO,
    private val contentResolver: ContentResolver
) {
    //val places: Flow<List<Place>> = placesDAO.getAllPlaces()

    suspend fun upsert(place: Place) {
        if (place.imageUri?.isNotEmpty() == true) {
            val imageUri = saveImageToStorage(
                Uri.parse(place.imageUri),
                contentResolver,
                "TravelDiary_Place${place.name}"
            )
        } else {
            //placesDAO.upsert(place)
        }
    }

    //suspend fun delete(place: Place) = placesDAO.deletePlace(place)
}
