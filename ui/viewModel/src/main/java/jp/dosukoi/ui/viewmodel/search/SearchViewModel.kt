package jp.dosukoi.ui.viewmodel.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.ui.viewmodel.common.LoadState

interface SearchPageListener {
    fun onSearchedItemClick(url: String)
}

class SearchViewModel @AssistedInject constructor(
    @Assisted private val searchPageListener: SearchPageListener
) : ViewModel(), SearchPageListener by searchPageListener {

    @AssistedFactory
    interface Factory {
        fun create(searchPageListener: SearchPageListener): SearchViewModel
    }

    val loadState = MutableLiveData<LoadState<State>>(LoadState.Loaded(State.Initialized))

    val searchWord = MutableLiveData<String>()

    fun onSearchWordChanged(text: String) {
        searchWord.value = text
    }

    fun onSearchButtonClick() {
        // TODO: call API
    }

    fun onRetryClick() {

    }

    sealed class State {
        object Initialized : State()
        class Data(val repositoryList: List<Repository>) : State()
        object Empty : State()
    }

    companion object {
        class Provider(
            private val factory: Factory,
            private val searchPageListener: SearchPageListener
        ) : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                factory.create(searchPageListener) as T
        }
    }
}