package jp.dosukoi.githubclient.domain.usecase.search

import jp.dosukoi.githubclient.domain.entity.search.Search
import jp.dosukoi.githubclient.domain.repository.search.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchDataUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {

    suspend fun execute(query: String?, page: Int): Search {
        return searchRepository.findRepositories(query, page)
    }
}
