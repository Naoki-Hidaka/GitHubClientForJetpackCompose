package jp.dosukoi.ui.viewmodel.search

import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.ui.viewmodel.common.LoadState

data class SearchUiState(
    val searchWord: String = "",
    val searchState: LoadState<SearchState> = LoadState.Loaded(SearchState.Initialized),
    val isSearchWordError: Boolean = false,
    val errors: List<Throwable> = emptyList()
)

sealed class SearchState {
    object Initialized : SearchState()
    data class Data(val repositoryList: List<Repository>, val hasMore: Boolean) : SearchState()
    object Empty : SearchState()
}
