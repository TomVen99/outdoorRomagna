package com.example.outdoorromagna.data.repositories

import com.example.outdoorromagna.data.database.PlacesDAO
import com.example.outdoorromagna.data.database.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class UsersRepository(private val placesDAO: PlacesDAO) {
    val users: Flow<List<User>> = placesDAO.getAllUser()

    suspend fun upsert(user: User) = placesDAO.upsertUser(user)

    suspend fun delete(user: User) = placesDAO.deleteUser(user)

    fun getUser(user: String) = placesDAO.getUser(user)

    suspend fun updateProfileImg(username: String, profileImg: String) {
        placesDAO.updateProfileImg(username, profileImg)
    }
}
