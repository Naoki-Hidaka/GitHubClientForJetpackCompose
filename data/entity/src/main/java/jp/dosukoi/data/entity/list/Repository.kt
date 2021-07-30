package jp.dosukoi.data.entity.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val id: Long,
    val fullName: String,
    val description: String?,
    val htmlUrl: String
) : Parcelable