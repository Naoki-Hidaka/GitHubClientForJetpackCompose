package jp.dosukoi.data.repository.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.search.SearchPageState
import jp.dosukoi.data.repository.common.asyncFetch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val api: IApiType
) {

    private val items = mutableListOf<Repository>()
    private val _searchData = MutableLiveData<SearchPageState>(SearchPageState.Initialized)
    val searchData: LiveData<SearchPageState> = _searchData

    suspend fun findRepositories(
        query: String?,
        page: Int,
        isRefresh: Boolean
    ) {
        if (isRefresh) items.clear()
        val item = asyncFetch { api.findRepositories(query, page) }
        items.addAll(item.items)
        if (item.items.isNotEmpty()) _searchData.value =
            SearchPageState.Data(items, item.totalCount > PER_PAGE * page)
        else _searchData.value = SearchPageState.Empty
    }

    companion object {
        private const val PER_PAGE = 30
    }
}
