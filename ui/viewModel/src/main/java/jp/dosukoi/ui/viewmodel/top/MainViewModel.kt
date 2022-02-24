package jp.dosukoi.ui.viewmodel.top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.usecase.auth.GetAccessTokenUseCase
import jp.dosukoi.ui.viewmodel.common.NoCacheMutableLiveData
import jp.dosukoi.ui.viewmodel.myPage.MyPageListener
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel(), MyPageListener {

    val onEvent = NoCacheMutableLiveData<Event>()

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

    sealed class Event {
        object CompleteGetAccessToken : Event()
        class FailedFetch(val throwable: Throwable) : Event()
    }
}
