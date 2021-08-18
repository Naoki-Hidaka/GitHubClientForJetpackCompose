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
        println("called execute")
        searchRepository.findRepositories(query, page, isRefresh)
    }
}
