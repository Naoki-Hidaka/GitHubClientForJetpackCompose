package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.User
import jp.dosukoi.data.repository.myPage.ReposRepository
import jp.dosukoi.data.repository.myPage.UserRepository
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.launch

interface MyPageListener {
    fun onLoginButtonClick()
    fun onCardClick(url: String)
    fun onRepositoryItemClick(url: String)
    fun onGetCode(code: String?)
}

class MyPageViewModel @AssistedInject constructor(
    private val userRepository: UserRepository,
    private val reposRepository: ReposRepository,
    @Assisted private val myPageListener: MyPageListener
) : ViewModel(), MyPageListener by myPageListener {

    @AssistedFactory
    interface Factory {
        fun create(myPageListener: MyPageListener): MyPageViewModel
    }

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

    companion object {
        @Suppress("UNCHECKED_CAST")
        class Provider(
            private val factory: Factory,
            private val myPageListener: MyPageListener
        ) : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                factory.create(myPageListener) as T
        }
    }
}
