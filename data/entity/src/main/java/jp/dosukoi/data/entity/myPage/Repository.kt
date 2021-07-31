package jp.dosukoi.data.entity.myPage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val id: Long,
    val fullName: String,
    val description: String?,
    val htmlUrl: String
) : Parcelable