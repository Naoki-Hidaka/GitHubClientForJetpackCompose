package jp.dosukoi.githubclient.domain.repository.auth

interface AuthRepository {
    suspend fun getAccessToken(code: String)
}
