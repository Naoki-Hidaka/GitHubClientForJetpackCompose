package jp.dosukoi.data.usecase.myPage

import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.repository.myPage.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserStatusUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend fun execute(): UserStatus = userRepository.getUser()
}
