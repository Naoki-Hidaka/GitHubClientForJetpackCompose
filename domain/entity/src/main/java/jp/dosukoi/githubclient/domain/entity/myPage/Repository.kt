package jp.dosukoi.githubclient.domain.entity.myPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Repository(
    val id: Long,
    @SerialName("full_name")
    val fullName: String,
    val description: String?,
    @SerialName("html_url")
    val htmlUrl: String
) : Parcelable
