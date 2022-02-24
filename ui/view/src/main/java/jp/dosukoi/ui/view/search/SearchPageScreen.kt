package jp.dosukoi.ui.view.search

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import jp.dosukoi.ui.view.common.OnScrollEnd
import jp.dosukoi.ui.viewmodel.search.SearchViewModel
import timber.log.Timber

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    Timber.d("debug: search screen")
    val uiState by viewModel.searchUiState.collectAsState()
    val listState = rememberLazyListState()
    OnScrollEnd(lazyListState = listState, onAppearLastItem = viewModel::onScrollEnd)
    SearchComponent(
        uiState,
        listState,
        viewModel::onSearchWordChanged,
        viewModel::onSearchButtonClick,
        viewModel::onRetryClick
    )
}
