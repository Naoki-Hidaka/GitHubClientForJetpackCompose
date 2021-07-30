package jp.dosukoi.data.api.common

import jp.dosukoi.data.entity.auth.AuthDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccessTokenProvider @Inject constructor(
    private val authDao: AuthDao
) {

    fun provide(): String? {
        return authDao.getAuth()?.accessToken
    }
}