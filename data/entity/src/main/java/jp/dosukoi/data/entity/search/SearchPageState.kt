package jp.dosukoi.data.entity.search

import jp.dosukoi.data.entity.myPage.Repository

sealed class SearchPageState {
    object Initialized : SearchPageState()
    class Data(val repositoryList: List<Repository>, val hasMore: Boolean) : SearchPageState()
    object Empty : SearchPageState()

}