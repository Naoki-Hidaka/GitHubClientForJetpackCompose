package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.repository.myPage.UserRepository
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val loadState = MutableLiveData<LoadState<UserStatus>>(LoadState.Loading)

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                userRepository.getUser()
            }
                .onSuccess {
                    it?.let {
                        loadState.value = LoadState.Loaded(UserStatus.Authenticated(it))
                    } ?: run {
                        loadState.value = LoadState.Error
                    }
                }.onFailure {
                    when (it) {
                        is UnAuthorizeException -> {
                            loadState.value = LoadState.Loaded(UserStatus.UnAuthenticated)
                        }
                        else -> {
                            loadState.value = LoadState.Error
                        }
                    }
                }
        }
    }

    fun onRetryClick() {
        loadState.value = LoadState.Loading
        refresh()
    }

    sealed class UserStatus {
        class Authenticated(val user: User) : UserStatus()
        object UnAuthenticated : UserStatus()
    }

    sealed class Event {
        class FailedFetch(val throwable: Throwable) : Event()
    }
}