package jp.dosukoi.githubclient.domain.entity.myPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val login: String,
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String,
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val company: String?,
    val email: String?,
    val bio: String?
) : Parcelable
