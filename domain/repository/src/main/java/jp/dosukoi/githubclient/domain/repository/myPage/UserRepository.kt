package jp.dosukoi.githubclient.domain.repository.myPage

import jp.dosukoi.githubclient.domain.entity.myPage.UserStatus

interface UserRepository {
    suspend fun getUser(): UserStatus
}
