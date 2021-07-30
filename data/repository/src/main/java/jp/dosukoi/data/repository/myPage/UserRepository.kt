package jp.dosukoi.data.repository.myPage

import dagger.Reusable
import jp.dosukoi.data.api.myPage.UserRemoteDataSource
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.local.myPage.UserLocalDataSource
import kotlinx.coroutines.delay
import retrofit2.HttpException
import javax.inject.Inject

@Reusable
class UserRepository @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend fun getUser(): User {
        val cachedData = userLocalDataSource.getUserCache()
        val cachedTime = cachedData?.cachedDate ?: 0L
        return if (cachedTime + 10000L < System.currentTimeMillis() || cachedData == null) {
            try {
                delay(1000)
                throw UnAuthorizeException("")
                userRemoteDataSource.getUser().body()!!.also {
                    userLocalDataSource.add(it)
                }
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        when (throwable.code()) {
                            401, 403 -> throw UnAuthorizeException("UnAuthorized\nPlease Login")
                            else -> throw throwable
                        }
                    }
                    else -> throw throwable
                }
            }
        } else {
            cachedData.user
        }
    }
}