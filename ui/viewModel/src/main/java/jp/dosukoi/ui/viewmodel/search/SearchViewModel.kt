package jp.dosukoi.ui.viewmodel.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.repository.search.SearchRepository
import jp.dosukoi.ui.viewmodel.common.LoadState
import jp.dosukoi.ui.viewmodel.common.NoCacheMutableLiveData
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

interface SearchPageListener {
    fun onSearchedItemClick(url: String)
}

class SearchViewModel @AssistedInject constructor(
    @Assisted private val searchPageListener: SearchPageListener,
    private val searchRepository: SearchRepository,
) : ViewModel(), SearchPageListener by searchPageListener {

    @AssistedFactory
    interface Factory {
        fun create(searchPageListener: SearchPageListener): SearchViewModel
    }

    val isError = NoCacheMutableLiveData<Boolean>()

    private val items = mutableListOf<Repository>()
    val loadState = MutableLiveData<LoadState<State>>(LoadState.Loaded(State.Initialized))
    val hasMore = MutableLiveData<Boolean>()
    private val isLoadingMore = AtomicBoolean()
    private val pageCount = AtomicInteger(1)

    val searchWord = MutableLiveData<String>()

    fun onSearchWordChanged(text: String) {
        searchWord.value = text
    }

    fun onSearchButtonClick() {
        if (searchWord.value.isNullOrBlank()) {
            isError.setValue(true)
            return
        } else isError.setValue(false)
        loadState.value = LoadState.Loading
        refresh()
    }

    fun onRetryClick() {
        if (searchWord.value.isNullOrBlank()) {
            isError.setValue(true)
            return
        } else isError.setValue(false)
        loadState.value = LoadState.Loading
        refresh()
    }

    fun onScrollEnd() {
        if (hasMore.value == true && isLoadingMore.compareAndSet(false, true)) {
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                searchRepository.findRepositories(searchWord.value!!, pageCount.getAndIncrement())
            }.onSuccess {
                hasMore.value = !it.incompleteResults
                isLoadingMore.set(false)
                items.addAll(it.items)

                if (it.items.isNotEmpty()) loadState.value = LoadState.Loaded(State.Data(items))
                else loadState.value = LoadState.Loaded(State.Empty)
            }.onFailure {
                loadState.value = LoadState.Error
            }
        }
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