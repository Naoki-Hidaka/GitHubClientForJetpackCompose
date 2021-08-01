package jp.dosukoi.ui.viewmodel.search

import androidx.annotation.VisibleForTesting
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
    fun onLoadError(throwable: Throwable)
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

    @VisibleForTesting
    val isLoadingMore = AtomicBoolean()
    private val pageCount = AtomicInteger(1)

    val searchWord = MutableLiveData<String>()

    fun onSearchWordChanged(text: String) {
        searchWord.value = text
    }

    fun onSearchButtonClick() {
        validateAndRefresh()
    }

    fun onRetryClick() {
        validateAndRefresh()
    }

    fun onScrollEnd() {
        if (hasMore.value == true && isLoadingMore.compareAndSet(false, true)) {
            refresh()
        }
    }

    private fun validateAndRefresh() {
        if (searchWord.value.isNullOrBlank()) {
            isError.setValue(true)
            return
        } else {
            items.clear()
            pageCount.set(0)
            isError.setValue(false)
        }
        loadState.value = LoadState.Loading
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            runCatching {
                searchRepository.findRepositories(searchWord.value, pageCount.getAndIncrement())
            }.onSuccess {
                hasMore.value = it.totalCount > PER_PAGE
                items.addAll(it.items)

                if (it.items.isNotEmpty()) loadState.value = LoadState.Loaded(State.Data(items))
                else loadState.value = LoadState.Loaded(State.Empty)
            }.onFailure {
                when (loadState.value) {
                    is LoadState.Loaded -> {
                        searchPageListener.onLoadError(it)
                    }
                    else -> loadState.value = LoadState.Error
                }
            }
            isLoadingMore.set(false)
        }
    }

    sealed class State {
        object Initialized : State()
        class Data(val repositoryList: List<Repository>) : State()
        object Empty : State()
    }

    companion object {
        private const val PER_PAGE = 30

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