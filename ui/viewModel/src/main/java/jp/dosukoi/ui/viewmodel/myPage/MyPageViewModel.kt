package jp.dosukoi.ui.viewmodel.myPage

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.common.LoadState
import jp.dosukoi.data.entity.myPage.MyPageState
import jp.dosukoi.data.usecase.myPage.GetMyPageUseCase
import kotlinx.coroutines.launch

interface MyPageListener {
    fun onLoginButtonClick()
    fun onCardClick(url: String)
    fun onRepositoryItemClick(url: String)
    fun onGetCode(code: String?)
}

class MyPageViewModel @AssistedInject constructor(
    private val getMyPageUseCase: GetMyPageUseCase,
    @Assisted private val myPageListener: MyPageListener
) : ViewModel(), MyPageListener by myPageListener {

    @AssistedFactory
    interface Factory {
        fun create(myPageListener: MyPageListener): MyPageViewModel
    }

    val loadState = MutableLiveData(LoadState.LOADING)

    val myPageState = MutableLiveData<MyPageState>()

    val isRefreshing = MutableLiveData<Boolean>()

    init {
        loadState.value = LoadState.LOADING
        refresh()
    }

    @VisibleForTesting
    fun refresh() {
        viewModelScope.launch {
            runCatching {
                myPageState.value = getMyPageUseCase.execute()
            }.onSuccess {
                loadState.value = LoadState.LOADED
            }.onFailure {
                loadState.value = LoadState.ERROR
            }
            isRefreshing.value = false
        }
    }

    fun onRetryClick() {
        loadState.value = LoadState.LOADING
        refresh()
    }

    fun onRefresh() {
        isRefreshing.value = true
        refresh()
    }
}
