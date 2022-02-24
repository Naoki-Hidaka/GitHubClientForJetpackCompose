package jp.dosukoi.ui.viewmodel.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.usecase.auth.GetAccessTokenUseCase
import jp.dosukoi.ui.viewmodel.common.NoCacheMutableLiveData
import jp.dosukoi.ui.viewmodel.myPage.MyPageListener
import jp.dosukoi.ui.viewmodel.search.SearchPageListener
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel(), MyPageListener, SearchPageListener {

    val onEvent = NoCacheMutableLiveData<Event>()

    override fun onLoginButtonClick() {
        onEvent.setValue(Event.NavigateToChrome(VERIFY_URL))
    }

    override fun onCardClick(url: String) {
        onEvent.setValue(Event.NavigateToChrome(url))
    }

    override fun onGetCode(code: String?) {
        code ?: return
        viewModelScope.launch {
            runCatching {
                getAccessTokenUseCase.execute(code)
            }.onSuccess {
                onEvent.setValue(Event.CompleteGetAccessToken)
            }.onFailure {
                onEvent.setValue(Event.FailedFetch(it))
            }
        }
    }

    override fun onSearchedItemClick(url: String) {
        onEvent.setValue(Event.NavigateToChrome(url))
    }

    override fun onLoadError(throwable: Throwable) {
        onEvent.setValue(Event.FailedFetch(throwable))
    }

    sealed class Event {
        object CompleteGetAccessToken : Event()
        class FailedFetch(val throwable: Throwable) : Event()
        class NavigateToChrome(val url: String) : Event()
    }

    companion object {
        private const val CLIENT_ID = "52b65f6025ea1e4264cd"
        const val VERIFY_URL =
            "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&scope=user repo"
    }
}
