package jp.dosukoi.githubclient.domain.usecase.myPage

import jp.dosukoi.githubclient.domain.entity.myPage.Repository
import jp.dosukoi.githubclient.domain.repository.myPage.ReposRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepositoriesUseCase @Inject constructor(
    private val reposRepository: ReposRepository
) {
    suspend fun execute(): List<Repository> = reposRepository.getRepositoryList()
}
