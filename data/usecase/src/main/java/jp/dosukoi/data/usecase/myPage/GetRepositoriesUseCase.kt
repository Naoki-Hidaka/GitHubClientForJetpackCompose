package jp.dosukoi.data.usecase.myPage

import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.repository.myPage.ReposRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepositoriesUseCase @Inject constructor(
    private val reposRepository: ReposRepository
) {
    suspend fun execute(): List<Repository> = reposRepository.getRepositoryList()
}
