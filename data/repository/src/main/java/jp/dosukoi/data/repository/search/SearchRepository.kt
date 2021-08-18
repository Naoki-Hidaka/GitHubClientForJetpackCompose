package jp.dosukoi.data.repository.search

import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.search.Search
import jp.dosukoi.data.repository.common.asyncFetch
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: IApiType
) {
    suspend fun findRepositories(
        query: String?,
        page: Int
    ): Search {
        return asyncFetch { api.findRepositories(query, page) }
    }
}
