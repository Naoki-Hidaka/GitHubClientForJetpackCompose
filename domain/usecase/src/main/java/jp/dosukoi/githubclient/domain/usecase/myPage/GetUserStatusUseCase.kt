package jp.dosukoi.githubclient.domain.usecase.myPage

import jp.dosukoi.githubclient.domain.entity.myPage.UserStatus
import jp.dosukoi.githubclient.domain.repository.myPage.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserStatusUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend fun execute(): UserStatus = userRepository.getUser()
}
