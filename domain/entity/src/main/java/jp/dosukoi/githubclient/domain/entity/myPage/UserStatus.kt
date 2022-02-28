package jp.dosukoi.githubclient.domain.entity.myPage

sealed class UserStatus {
    class Authenticated(val user: User) : UserStatus()
    object UnAuthenticated : UserStatus()
}
