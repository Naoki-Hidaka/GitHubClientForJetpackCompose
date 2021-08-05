package jp.dosukoi.data.repository.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import jp.dosukoi.data.api.common.IApiType
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.repository.common.asyncFetch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: IApiType
) {
    private val _userStatus = MutableLiveData<UserStatus>()
    val userStatus: LiveData<UserStatus> = _userStatus

    suspend fun getUser() {
        try {
            _userStatus.value = UserStatus.Authenticated(asyncFetch { api.getUser() })
        } catch (throwable: Throwable) {
            Timber.e("error: $throwable")
            if (throwable is HttpException) {
                when (throwable.code()) {
                    401, 403 -> _userStatus.value = UserStatus.UnAuthenticated
                    else -> throw throwable
                }
            } else {
                throw throwable
            }
        }
    }
}