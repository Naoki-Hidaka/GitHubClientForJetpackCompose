package jp.dosukoi.data.usecase.myPage

import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.repository.myPage.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyPageUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository
) {

    suspend fun execute(): MyPageState {
        val userStatus = userRepository.getUser()
        val repositoryList = reposRepository.getRepositoryList()
        return MyPageState(userStatus, repositoryList)
    }
}
