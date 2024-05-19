package com.example.outdoorromagna.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDAO {
    @Query("SELECT * FROM user ORDER BY username ASC")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM marker ORDER BY name ASC")
    fun getAllMarker(): Flow<List<Marker>>

    @Query("SELECT * FROM favorite")
    fun getAllFavorite(): Flow<List<Favorite>>

    @Query("SELECT * FROM user WHERE username = :user")
    fun getUser(user: String): Flow<User?>

    @Query("SELECT * FROM marker WHERE latitude = :latitude AND longitude = :longitude")
    fun getMarker(latitude: Float, longitude: Float): Flow<Marker?>

    @Query("SELECT * FROM favorite WHERE userId = :userID AND markerId = :markerId")
    fun getFavorite(userID : Int, markerId : Int) : Flow<Favorite?>

    @Query("UPDATE user SET urlProfilePicture = :profileImg WHERE username = :username")
    suspend fun updateProfileImg(username: String, profileImg: String)

    /*@Query("SELECT * FROM place ORDER BY name ASC")
    fun getAllPlaces(): Flow<List<Place>>*/
    @Upsert
    suspend fun upsertUser(user: User)

    @Upsert
    suspend fun upsertMarker(marker: Marker)

    @Upsert
    suspend fun upsertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteUser(item: User)

    /*@Delete
    suspend fun deletePlace(item: Place)*/

    @Delete
    suspend fun deleteMarker(item: Marker)

    @Delete
    suspend fun deleteFavorite(item : Favorite)
}
