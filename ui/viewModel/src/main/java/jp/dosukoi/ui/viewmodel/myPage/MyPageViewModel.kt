package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.githubclient.domain.entity.auth.UnAuthorizeException
import jp.dosukoi.githubclient.domain.entity.myPage.UserStatus
import jp.dosukoi.githubclient.domain.usecase.auth.GetAccessTokenUseCase
import jp.dosukoi.githubclient.domain.usecase.myPage.GetRepositoriesUseCase
import jp.dosukoi.githubclient.domain.usecase.myPage.GetUserStatusUseCase
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getUserStatusUseCase: GetUserStatusUseCase,
    private val getRepositoriesUseCase: GetRepositoriesUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ViewModel() {

    private val _myPageState: MutableStateFlow<MyPageUiState> =
        MutableStateFlow(MyPageUiState())
    val myPageState: StateFlow<MyPageUiState> = _myPageState

    fun init() {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                MyPageScreenState(
                    getUserStatusUseCase.execute(),
                    getRepositoriesUseCase.execute(),
                )
            }.onSuccess { screenState ->
                _myPageState.update {
                    it.copy(screenState = LoadState.Loaded(screenState), isRefreshing = false)
                }
            }.onFailure {
                Timber.w(it)
                when (it) {
                    is UnAuthorizeException -> {
                        _myPageState.update {
                            it.copy(
                                screenState = LoadState.Loaded(
                                    MyPageScreenState(
                                        UserStatus.UnAuthenticated,
                                        emptyList()
                                    )
                                ),
                                isRefreshing = false
                            )
                        }
                    }
                    else -> {
                        _myPageState.update {
                            it.copy(screenState = LoadState.Error("Error"))
                        }
                    }
                }
            }
        }
    }

    fun onRetryClick() {
        _myPageState.update {
            it.copy(screenState = LoadState.Loading)
        }
        refresh()
    }

    fun onRefresh() {
        _myPageState.update {
            when (it.screenState) {
                is LoadState.Loaded -> it.copy(isRefreshing = true)
                else -> it
            }
        }
        refresh()
    }

    fun onGetCode(code: String?) {
        code ?: return
        viewModelScope.launch {
            runCatching {
                getAccessTokenUseCase.execute(code)
            }.onSuccess {
                _myPageState.update {
                    it.copy(screenState = LoadState.Loading)
                }
                refresh()
            }.onFailure { throwable ->
                _myPageState.update {
                    it.copy(errors = it.errors.plus(throwable))
                }
            }
        }
    }

    fun onConsumeErrors(throwable: Throwable) {
        _myPageState.update {
            it.copy(errors = it.errors.minus(throwable))
        }
    }
}
