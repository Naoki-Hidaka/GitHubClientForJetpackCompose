package jp.dosukoi.data.local.myPage

import jp.dosukoi.data.entity.myPage.User
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLocalDataSource @Inject constructor() : ArrayList<UserCache>(LIMIT) {

    companion object {
        const val LIMIT = 3
    }

    fun getUserCache() = this.lastOrNull()

    override fun add(element: UserCache): Boolean {
        return if (this.size == LIMIT) {
            this.removeAt(0)
            super.add(element)
        } else {
            super.add(element)
        }
    }

    fun add(element: User): Boolean {
        val userCache = UserCache(System.currentTimeMillis(), element)
        return add(userCache)
    }
}

data class UserCache(
    val cachedDate: Long,
    val user: User
)