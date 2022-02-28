package jp.dosukoi.githubclient.domain.entity.search

import android.os.Parcelable
import jp.dosukoi.githubclient.domain.entity.myPage.Repository
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Search(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    val items: List<Repository>
) : Parcelable
