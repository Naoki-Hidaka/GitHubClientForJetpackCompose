package jp.dosukoi.data.entity.myPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val login: String,
    val id: Long,
    val avatarUrl: String,
    val url: String,
    val htmlUrl: String,
    val company: String?,
    val email: String?,
    val bio: String?
) : Parcelable