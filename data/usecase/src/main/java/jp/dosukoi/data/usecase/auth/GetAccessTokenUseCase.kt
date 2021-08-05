package jp.dosukoi.data.usecase.auth

import dagger.Reusable
import jp.dosukoi.data.repository.auth.AuthRepository
import javax.inject.Inject

@Reusable
class GetAccessTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun execute(code: String) {
        authRepository.getAccessToken(code)
    }
}