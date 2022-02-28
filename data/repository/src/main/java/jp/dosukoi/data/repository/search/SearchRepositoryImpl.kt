package jp.dosukoi.data.repository.search

import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.repository.common.asyncFetch
import jp.dosukoi.githubclient.domain.entity.search.Search
import jp.dosukoi.githubclient.domain.repository.search.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val api: IApiType
) : SearchRepository {

    override suspend fun findRepositories(
        query: String?,
        page: Int,
    ): Search {
        return asyncFetch { api.findRepositories(query, page) }
    }

    companion object {
        private const val PER_PAGE = 30
    }
}
