package com.example.outdoorromagna.data.repositories

import com.example.outdoorromagna.data.database.UsersDAO
import com.example.outdoorromagna.data.database.User
import kotlinx.coroutines.flow.Flow

class UsersRepository(private val usersDAO: UsersDAO) {
    val users: Flow<List<User>> = usersDAO.getAllUser()

    suspend fun upsert(user: User) = usersDAO.upsertUser(user)

    suspend fun delete(user: User) = usersDAO.deleteUser(user)

    fun getUser(user: String) = usersDAO.getUser(user)
}
