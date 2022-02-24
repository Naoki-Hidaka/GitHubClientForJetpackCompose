package jp.dosukoi.ui.viewmodel.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.dosukoi.data.entity.search.Search
import jp.dosukoi.data.usecase.search.GetSearchDataUseCase
import jp.dosukoi.ui.viewmodel.common.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchDataUseCase: GetSearchDataUseCase
) : ViewModel() {

    private val _searchUiState: MutableStateFlow<SearchUiState> =
        MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState

    @VisibleForTesting
    val isLoadingMore = AtomicBoolean()
    private val pageCount = AtomicInteger(1)

    fun onSearchWordChanged(text: String) {
        _searchUiState.update {
            it.copy(searchWord = text)
        }
    }

    fun onSearchButtonClick() {
        validateAndRefresh()
    }

    fun onRetryClick() {
        validateAndRefresh()
    }

    fun onScrollEnd() {
        when (val loadState = searchUiState.value.searchState) {
            is LoadState.Loaded -> {
                when (val searchState = loadState.data) {
                    is SearchState.Data -> {
                        if (searchState.hasMore && isLoadingMore.compareAndSet(false, true)) {
                            refresh(false)
                        }
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
            else -> {
                // do nothing
            }
        }
    }

    fun onConsumeErrors(throwable: Throwable) {
        _searchUiState.update {
            it.copy(errors = it.errors.minus(throwable))
        }
    }

    private fun validateAndRefresh() {
        if (searchUiState.value.searchWord.isBlank()) {
            _searchUiState.update {
                it.copy(isSearchWordError = true)
            }
            return
        } else {
            pageCount.set(1)
            _searchUiState.update {
                it.copy(isSearchWordError = false)
            }
        }
        _searchUiState.update {
            it.copy(searchState = LoadState.Loading)
        }
        refresh(true)
    }

    private fun refresh(isRefresh: Boolean) {
        viewModelScope.launch {
            runCatching {
                getSearchDataUseCase.execute(
                    searchUiState.value.searchWord,
                    pageCount.getAndIncrement(),
                )
            }.onSuccess {
                if (it.items.isNotEmpty()) {
                    updateUiState(isRefresh, it)
                } else {
                    _searchUiState.update { uiState ->
                        when (val loadState = uiState.searchState) {
                            is LoadState.Loaded -> {
                                val searchState = when (val state = loadState.data) {
                                    is SearchState.Data -> state.copy(
                                        hasMore = false
                                    )
                                    else -> SearchState.Empty
                                }
                                uiState.copy(searchState = loadState.copy(data = searchState))
                            }
                            else -> uiState
                        }
                    }
                }
            }.onFailure {
                _searchUiState.update { uiState ->
                    when (uiState.searchState) {
                        is LoadState.Loaded -> {
                            uiState.copy(errors = uiState.errors.plus(it))
                        }
                        else -> {
                            uiState.copy(searchState = LoadState.Error("Error"))
                        }
                    }
                }
            }
            isLoadingMore.set(false)
        }
    }

    private fun updateUiState(isRefresh: Boolean, search: Search) {
        val hasMore = search.totalCount > PER_PAGE * pageCount.get()
        _searchUiState.update { uiState ->
            when (val loadState = uiState.searchState) {
                is LoadState.Loaded -> {
                    when (val searchState = loadState.data) {
                        is SearchState.Data -> {
                            val repositoryList = if (isRefresh) {
                                search.items
                            } else {
                                searchState.repositoryList + search.items
                            }
                            uiState.copy(
                                searchState = loadState.copy(
                                    data = searchState.copy(
                                        repositoryList = repositoryList,
                                        hasMore = hasMore
                                    )
                                )
                            )
                        }
                        else -> {
                            uiState.copy(
                                searchState = loadState.copy(
                                    data = SearchState.Data(
                                        repositoryList = search.items,
                                        hasMore = hasMore
                                    )
                                )
                            )
                        }
                    }
                }
                else -> {
                    uiState.copy(
                        searchState = LoadState.Loaded(
                            data = SearchState.Data(
                                repositoryList = search.items,
                                hasMore = hasMore
                            )
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val PER_PAGE = 30
    }
}
