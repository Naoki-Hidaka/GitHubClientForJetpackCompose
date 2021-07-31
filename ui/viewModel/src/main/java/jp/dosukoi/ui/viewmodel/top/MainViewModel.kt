package jp.dosukoi.ui.viewmodel.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.repository.auth.AuthRepository
import jp.dosukoi.ui.viewmodel.common.NoCacheMutableLiveData
import jp.dosukoi.ui.viewmodel.myPage.MyPageListener
import jp.dosukoi.ui.viewmodel.search.SearchPageListener
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), MyPageListener, SearchPageListener {

    val onEvent = NoCacheMutableLiveData<Event>()

    override fun onLoginButtonClick() {
        onEvent.setValue(Event.ClickedLoginButton)
    }

    override fun onCardClick(url: String) {
        onEvent.setValue(Event.ClickedCard(url))
    }

    override fun onRepositoryItemClick(url: String) {
        onEvent.setValue(Event.ClickedRepositoryItem(url))
    }

    override fun onGetCode(code: String?) {
        code ?: return
        viewModelScope.launch {
            runCatching {
                authRepository.getAccessToken(code)
            }.onSuccess {
                onEvent.setValue(Event.CompleteGetAccessToken)
            }.onFailure {
                onEvent.setValue(Event.FailedGetAccessToken(it))
            }
        }
    }

    override fun onSearchedItemClick(url: String) {
        TODO("Not yet implemented")
    }

    sealed class Event {
        object ClickedLoginButton : Event()
        object CompleteGetAccessToken : Event()
        class FailedGetAccessToken(val throwable: Throwable) : Event()
        class ClickedCard(val url: String) : Event()
        class ClickedRepositoryItem(val url: String) : Event()
    }

    companion object {
        private const val CLIENT_ID = "52b65f6025ea1e4264cd"
        const val VERIFY_URL =
            "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&scope=user repo"
    }
}