package jp.dosukoi.data.repository.myPage

import dagger.Reusable
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.repository.common.asyncFetch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

@Reusable
class UserRepository @Inject constructor(
    private val api: IApiType
) {
    suspend fun getUser(): User {
        return try {
            asyncFetch { api.getUser() }
        } catch (throwable: Throwable) {
            Timber.e("error: $throwable")
            if (throwable is HttpException) {
                when (throwable.code()) {
                    401, 403 -> throw UnAuthorizeException("UnAuthorize\nPlease Login")
                    else -> throw throwable
                }
            } else {
                throw throwable
            }
        }
    }
}