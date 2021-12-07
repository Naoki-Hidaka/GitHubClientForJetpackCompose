package jp.dosukoi.data.entity.myPage

sealed class UserStatus {
    class Authenticated(val user: User) : UserStatus()
    object UnAuthenticated : UserStatus()
}
