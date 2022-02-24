package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.common.UnAuthorizeException
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.data.usecase.myPage.GetRepositoriesUseCase
import jp.dosukoi.data.usecase.myPage.GetUserStatusUseCase
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface MyPageListener {
    fun onGetCode(code: String?)
}

class MyPageViewModel @AssistedInject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    @Assisted private val myPageListener: MyPageListener
) : ViewModel(), MyPageListener by myPageListener {

    @AssistedFactory
    interface Factory {
        fun create(myPageListener: MyPageListener): MyPageViewModel
    }

    private val _myPageState: MutableStateFlow<LoadState<MyPageUiState>> =
        MutableStateFlow(LoadState.Loading)
    val myPageState: StateFlow<LoadState<MyPageUiState>> = _myPageState

    fun init() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                MyPageUiState(
                    getUserStatusUseCase.execute(),
                    getRepositoriesUseCase.execute(),
                    false
                )
            }.onSuccess {
                _myPageState.value = LoadState.Loaded(it)
            }.onFailure {
                when (it) {
                    is UnAuthorizeException -> {
                        _myPageState.value = LoadState.Loaded(
                            MyPageUiState(
                                UserStatus.UnAuthenticated,
                                emptyList(),
                                isRefreshing = false
                            )
                        )
                    }
                    else -> {
                        _myPageState.value = LoadState.Error("error")
                    }
                }
            }
        }
    }

    fun onRetryClick() {
        _myPageState.value = LoadState.Loading
        refresh()
    }

    fun onRefresh() {
        _myPageState.update {
            when (it) {
                is LoadState.Loaded -> it.copy(data = it.data.copy(isRefreshing = true))
                else -> it
            }
        }
        refresh()
    }
}
