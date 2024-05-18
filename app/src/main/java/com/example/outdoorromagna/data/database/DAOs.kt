package com.example.outdoorromagna.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDAO {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>

    @Query("SELECT * FROM user WHERE username = :user")
    fun getUser(user: String): Flow<User?>

    /*@Query("SELECT * FROM Track WHERE start = :latitude AND longitude = :longitude")
    fun getTrackByLatLng(latitude: Float, longitude: Float): Flow<Track?>*/

    @Query("SELECT * FROM favorite WHERE userId = :userID AND markerId = :markerId")
    fun getFavorite(userID : Int, markerId : Int) : Flow<Favorite?>

    /*@Query("SELECT * FROM place ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<Place>>*/
    @Upsert
    suspend fun upsertUser(user: User)


    @Upsert
    suspend fun upsertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteUser(item: User)

    /*@Delete
    suspend fun deletePlace(item: Place)*/



    @Delete
    suspend fun deleteFavorite(item : Favorite)
}

@Dao
interface TracksDAO {
    @Upsert
    suspend fun upsertTrack(track: Track)

    @Query("SELECT * FROM Track ")
    fun getAllTracks(): Flow<List<Track>>

    @Delete
    suspend fun deleteTrack(track: Track)
}

