package com.example.outdoorromagna.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,


    @ColumnInfo
    var username: String,

    @ColumnInfo
    var password: String,

    @ColumnInfo
    var urlProfilePicture : String,

    @ColumnInfo
    var salt : ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (urlProfilePicture != other.urlProfilePicture) return false
        return salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + urlProfilePicture.hashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

@Entity
data class Marker (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    var city: String,

    @ColumnInfo
    var province: String,

    @ColumnInfo
    var latitude: Float,

    @ColumnInfo
    var longitude: Float
)

@Entity(primaryKeys = ["userId", "markerId"])
data class Favorite (
    @ColumnInfo
    var userId : Int,

    @ColumnInfo
    var markerId : Int
)

@Entity
data class Place (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val date: String,

    @ColumnInfo
    val description: String,

    @ColumnInfo
    val imageUri: String?
)
