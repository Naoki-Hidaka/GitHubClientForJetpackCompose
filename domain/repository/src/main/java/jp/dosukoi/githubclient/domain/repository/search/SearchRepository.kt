package jp.dosukoi.githubclient.domain.repository.search

import jp.dosukoi.githubclient.domain.entity.search.Search

interface SearchRepository {
    suspend fun findRepositories(query: String?, page: Int): Search
}
