package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.list.Repository
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.repository.list.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository
) : ViewModel() {

    val loadState = MutableLiveData<LoadState<UserStatus>>(LoadState.Loading)

    val isRefreshing = MutableLiveData<Boolean>()

    init {
        loadState.value = LoadState.Loading
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                RenderItem(
                    userRepository.getUser(),
                    reposRepository.getRepositoryList()
                )
            }
                .onSuccess {
                    loadState.value = LoadState.Loaded(UserStatus.Authenticated(it))
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
            isRefreshing.value = false
        }
    }

    fun onRetryClick() {
        loadState.value = LoadState.Loading
        refresh()
    }

    fun onRefresh() {
        isRefreshing.value = true
        refresh()
    }

    sealed class UserStatus {
        class Authenticated(val item: RenderItem) : UserStatus()
        object UnAuthenticated : UserStatus()
    }

    data class RenderItem(
        val user: User,
        val repositoryList: List<Repository>
    )
}