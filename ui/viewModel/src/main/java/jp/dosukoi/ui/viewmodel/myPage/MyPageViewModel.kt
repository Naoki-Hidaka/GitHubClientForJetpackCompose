package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.usecase.myPage.GetMyPageUseCase
import jp.dosukoi.data.usecase.myPage.GetRepositoriesUseCase
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.launch

interface MyPageListener {
    fun onLoginButtonClick()
    fun onCardClick(url: String)
    fun onRepositoryItemClick(url: String)
    fun onGetCode(code: String?)
}

class MyPageViewModel @AssistedInject constructor(
    private val getMyPageUseCase: GetMyPageUseCase,
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    @Assisted private val myPageListener: MyPageListener
) : ViewModel(), MyPageListener by myPageListener {

    @AssistedFactory
    interface Factory {
        fun create(myPageListener: MyPageListener): MyPageViewModel
    }

    val myPageState = MutableLiveData<LoadState<MyPageState>>()

    fun init() {
        myPageState.value = LoadState.Loading
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                MyPageState(
                    getMyPageUseCase.execute(),
                    getRepositoriesUseCase.execute(),
                    false
                )
            }.onSuccess {
                myPageState.value = LoadState.Loaded(it)
            }.onFailure {
                when (it) {
                    is UnAuthorizeException -> {
                        myPageState.value = LoadState.Loaded(
                            MyPageState(
                                UserStatus.UnAuthenticated,
                                emptyList(),
                                isRefreshing = false
                            )
                        )
                    }
                    else -> {
                        myPageState.value = LoadState.Error("error")
                    }
                }
            }
        }
    }

    fun onRetryClick() {
        myPageState.value = LoadState.Loading
        refresh()
    }

    fun onRefresh() {
        myPageState.value = when (val state = myPageState.value) {
            is LoadState.Loaded -> state.copy(data = state.data.copy(isRefreshing = true))
            else -> state
        }
        refresh()
    }

    data class MyPageState(
        val userStatus: UserStatus,
        val repositoryList: List<Repository>,
        val isRefreshing: Boolean
    )
}
