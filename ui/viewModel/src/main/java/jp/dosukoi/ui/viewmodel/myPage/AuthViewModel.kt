package jp.dosukoi.ui.viewmodel.myPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.ui.viewmodel.common.LoadState
import jp.dosukoi.ui.viewmodel.common.NoCacheMutableLiveData
import jp.dosukoi.ui.viewmodel.common.WebViewCallback
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel(), WebViewCallback {

    val onEvent = NoCacheMutableLiveData<Event>()
    val loadState = MutableLiveData<LoadState<Unit>>()

    private val data = "client=52b65f6025ea1e4264cd"
    val postData = data.toByteArray()

    fun onNavigationClick() {
        onEvent.setValue(Event.ClickedNavigation)
    }

    override fun onPageStarted() {
        loadState.value = LoadState.Loading
    }

    override fun onPageFinished() {
        loadState.value = LoadState.Loaded(Unit)
    }

    sealed class Event {
        object ClickedNavigation : Event()
    }

    companion object {
        const val VERIFY_URL =
            "https://github.com/login/device/code"
    }
}