package jp.dosukoi.data.repository.auth

import dagger.Reusable
import jp.dosukoi.data.api.common.IAuthApiType
import jp.dosukoi.data.repository.common.asyncFetch
import jp.dosukoi.githubclient.domain.entity.auth.AuthDao
import jp.dosukoi.githubclient.domain.entity.auth.AuthEntity
import jp.dosukoi.githubclient.domain.repository.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Named

@Reusable
class AuthRepositoryImpl @Inject constructor(
    @Named("clientId") private val clientId: String,
    @Named("clientSecret") private val clientSecret: String,
    private val api: IAuthApiType,
    private val authDao: AuthDao
) : AuthRepository {

    override suspend fun getAccessToken(code: String) {
        asyncFetch({ api.getAccessToken(clientId, clientSecret, code) }) {
            authDao.insert(AuthEntity(0, it.accessToken))
        }
    }
}
