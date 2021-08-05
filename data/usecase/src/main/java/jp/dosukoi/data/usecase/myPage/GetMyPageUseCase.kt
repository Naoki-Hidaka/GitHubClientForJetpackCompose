package jp.dosukoi.data.usecase.myPage

import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.repository.common.zip
import jp.dosukoi.data.repository.myPage.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyPageUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository
) {

    private val userStatus = userRepository.userStatus
    private val repositoryList = reposRepository.repositoryList

    val myPageState = zip(userStatus, repositoryList) { userStatus, repositories ->
        MyPageState(userStatus, repositories)
    }

    suspend fun execute() {
        userRepository.getUser()
        reposRepository.getRepositoryList()
    }

}