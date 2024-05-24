package com.example.outdoorromagna.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable
import java.util.UUID

@Entity
@Serializable
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var username: String,

    @ColumnInfo
    var password: String,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var surname: String,

    @ColumnInfo
    var mail: String,

    @ColumnInfo
    var urlProfilePicture : String?,

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
data class Track (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    var city: String,

    @ColumnInfo
    var startLat: Double,

    @ColumnInfo
    var startLng: Double,

    @ColumnInfo
    var trackPositions: List<LatLng>,

    @ColumnInfo
    val imageUri: String?,

    @ColumnInfo
    var duration: Long,

    @ColumnInfo
    val userId: Int,
)


@Entity(tableName = "activity")
data class Activity(
    @PrimaryKey val activityId: String = UUID.randomUUID().toString(),
    @ColumnInfo var userCreatorUsername: String,
    @ColumnInfo var name: String?,
    @ColumnInfo var description: String?,
    @ColumnInfo var totalTime: Long,
    @ColumnInfo var distance: Int,
    @ColumnInfo var speed: Double,
    @ColumnInfo var pace: Double,
    @ColumnInfo var steps: Int?,
    @ColumnInfo var onFoot: Boolean?,
    @ColumnInfo var favourite: Boolean,
    @ColumnInfo var date: String
)
