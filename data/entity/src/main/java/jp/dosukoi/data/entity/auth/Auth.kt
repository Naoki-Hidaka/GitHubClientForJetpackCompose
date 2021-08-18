package jp.dosukoi.data.entity.auth

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Auth(
    val accessToken: String
) : Parcelable

@Entity
data class AuthEntity(
    @PrimaryKey
    val id: Long,
    val accessToken: String
)
