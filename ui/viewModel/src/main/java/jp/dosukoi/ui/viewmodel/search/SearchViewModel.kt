package jp.dosukoi.ui.viewmodel.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.common.LoadState
import jp.dosukoi.data.entity.search.SearchPageState
import jp.dosukoi.data.usecase.search.GetSearchDataUseCase
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
    private val getSearchDataUseCase: GetSearchDataUseCase
) : ViewModel(), SearchPageListener by searchPageListener {

    @AssistedFactory
    interface Factory {
        fun create(searchPageListener: SearchPageListener): SearchViewModel
    }

    val isError = NoCacheMutableLiveData<Boolean>()

    val searchData = getSearchDataUseCase.searchData

    val loadState = MutableLiveData(LoadState.LOADED)
    val hasMore = searchData.map {
        if (it is SearchPageState.Data) it.hasMore
        else false
    }

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
            refresh(false)
        }
    }

    private fun validateAndRefresh() {
        if (searchWord.value.isNullOrBlank()) {
            isError.setValue(true)
            return
        } else {
            pageCount.set(1)
            isError.setValue(false)
        }
        loadState.value = LoadState.LOADING
        refresh(true)
    }

    private fun refresh(isRefresh: Boolean) {
        viewModelScope.launch {
            runCatching {
                getSearchDataUseCase.execute(
                    searchWord.value,
                    pageCount.getAndIncrement(),
                    isRefresh
                )
            }.onSuccess {
                loadState.value = LoadState.LOADED
            }.onFailure {
                when (loadState.value) {
                    LoadState.LOADED -> {
                        searchPageListener.onLoadError(it)
                    }
                    else -> loadState.value = LoadState.ERROR
                }
            }
            isLoadingMore.set(false)
        }
    }
}