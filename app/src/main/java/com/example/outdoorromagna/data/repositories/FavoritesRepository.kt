package com.example.outdoorromagna.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.outdoorromagna.data.database.Favourite
import com.example.outdoorromagna.data.database.FavouritesDAO
import com.example.outdoorromagna.data.database.Track


data class FavoritesRepository(
    private val favoritesDAO: FavouritesDAO,
){
    suspend fun upsert(favourite: Favourite) = favoritesDAO.upsertFavourite(favourite)

    suspend fun delete(favourite: Favourite) = favoritesDAO.deteleFavourite(favourite)

    suspend fun getFavouritesByUser(userId: Int) = favoritesDAO.getFavouritesByUser(userId)

}
