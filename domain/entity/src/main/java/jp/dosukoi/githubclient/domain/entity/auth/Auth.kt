package jp.dosukoi.githubclient.domain.entity.auth

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Auth(
    @SerialName("access_token")
    val accessToken: String
) : Parcelable

@Entity
data class AuthEntity(
    @PrimaryKey
    val id: Long,
    val accessToken: String
)
