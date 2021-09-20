package jp.dosukoi.data.usecase.myPage

import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.repository.myPage.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMyPageUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository
) {

    suspend fun execute(): MyPageState = coroutineScope {
        val userStatus = async { userRepository.getUser() }
        val repositoryList = async { reposRepository.getRepositoryList() }
        MyPageState(userStatus.await(), repositoryList.await())
    }
}
