package jp.dosukoi.data.usecase.search

import jp.dosukoi.data.repository.search.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    val searchData = searchRepository.searchData

    suspend fun execute(query: String?, page: Int, isRefresh: Boolean) {
        searchRepository.findRepositories(query, page, isRefresh)
    }

    companion object {
        private const val PER_PAGE = 30
    }
}