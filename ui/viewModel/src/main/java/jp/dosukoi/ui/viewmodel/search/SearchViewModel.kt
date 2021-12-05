package jp.dosukoi.ui.viewmodel.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.search.SearchPageState
import jp.dosukoi.data.usecase.search.GetSearchDataUseCase
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
    private val getSearchDataUseCase: GetSearchDataUseCase
) : ViewModel(), SearchPageListener by searchPageListener {

    @AssistedFactory
    interface Factory {
        fun create(searchPageListener: SearchPageListener): SearchViewModel
    }

    val isError = NoCacheMutableLiveData<Boolean>()

    private val items = mutableListOf<Repository>()
    val searchData = MutableLiveData<LoadState<SearchPageState>>(LoadState.Loading)

    val hasMore = searchData.map {
        when (val state = it) {
            is LoadState.Loaded -> when (val searchState = state.data) {
                is SearchPageState.Data -> searchState.hasMore
                else -> false
            }
            else -> false
        }
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
        searchData.value = LoadState.Loading
        refresh(true)
    }

    private fun refresh(isRefresh: Boolean) {
        viewModelScope.launch {
            runCatching {
                getSearchDataUseCase.execute(
                    searchWord.value,
                    pageCount.getAndIncrement(),
                )
            }.onSuccess {
                if (isRefresh) items.clear()
                if (it.items.isNotEmpty()) {
                    items.addAll(it.items)
                    searchData.value =
                        LoadState.Loaded(
                            SearchPageState.Data(
                                items,
                                it.totalCount > PER_PAGE * pageCount.get()
                            )
                        )
                } else {
                    when (searchData.value) {
                        is LoadState.Loaded -> {
                            searchData.value = LoadState.Loaded(SearchPageState.Data(items, false))
                        }
                        else -> {
                            searchData.value = LoadState.Loaded(SearchPageState.Empty)
                        }
                    }
                }
            }.onFailure {
                when (searchData.value) {
                    is LoadState.Loaded -> {
                        searchPageListener.onLoadError(it)
                    }
                    else -> searchData.value = LoadState.Error("error")
                }
            }
            isLoadingMore.set(false)
        }
    }

    companion object {
        private const val PER_PAGE = 30
    }
}
