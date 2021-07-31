package jp.dosukoi.data.entity.search

import android.os.Parcelable
import jp.dosukoi.data.entity.myPage.Repository
import kotlinx.parcelize.Parcelize

@Parcelize
data class Search(
    val totalCount: Int,
    val incompleteResults: Boolean,
    val items: List<Repository>
) : Parcelable