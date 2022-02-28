package jp.dosukoi.data.repository.myPage

import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.repository.common.asyncFetch
import jp.dosukoi.githubclient.domain.entity.auth.UnAuthorizeException
import jp.dosukoi.githubclient.domain.entity.myPage.UserStatus
import jp.dosukoi.githubclient.domain.repository.myPage.UserRepository
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: IApiType
) : UserRepository {

    override suspend fun getUser(): UserStatus {
        return try {
            UserStatus.Authenticated(asyncFetch { api.getUser() })
        } catch (throwable: Throwable) {
            if (throwable is HttpException) {
                when (throwable.code()) {
                    401, 403 -> throw UnAuthorizeException(throwable.message())
                    else -> throw throwable
                }
            } else {
                throw throwable
            }
        }
    }
}
