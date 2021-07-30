package jp.dosukoi.data.entity.myPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class User(
    val name: String,
    val id: Long,
    @SerialName("avatar_url")
    val avatarUrl: String,
    val url: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val company: String,
    val email: String
) : Parcelable