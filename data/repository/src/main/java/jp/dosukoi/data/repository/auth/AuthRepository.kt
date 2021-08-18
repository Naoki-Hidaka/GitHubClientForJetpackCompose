package jp.dosukoi.data.repository.auth

import dagger.Reusable
import jp.dosukoi.data.api.common.IAuthApiType
import jp.dosukoi.data.entity.auth.AuthDao
import jp.dosukoi.data.entity.auth.AuthEntity
import jp.dosukoi.data.repository.common.asyncFetch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

@Reusable
class AuthRepository @Inject constructor(
    @Named("clientId") private val clientId: String,
    @Named("clientSecret") private val clientSecret: String,
    private val api: IAuthApiType,
    private val authDao: AuthDao
) {

    suspend fun getAccessToken(code: String) {
        asyncFetch({ api.getAccessToken(clientId, clientSecret, code) }) {
            authDao.insert(AuthEntity(0, it.accessToken))
        }
        try {
            val response = api.getAccessToken(clientId, clientSecret, code)
            if (response.isSuccessful) {
                authDao.insert(AuthEntity(0, response.body()!!.accessToken))
            } else {
                throw HttpException(response)
            }
        } catch (throwable: Throwable) {
            throw throwable
        }
    }
}
