package jp.dosukoi.githubclient.domain.repository.myPage

import jp.dosukoi.githubclient.domain.entity.myPage.Repository

interface ReposRepository {
    suspend fun getRepositoryList(): List<Repository>
}
