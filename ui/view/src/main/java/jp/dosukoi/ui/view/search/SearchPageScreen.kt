package jp.dosukoi.ui.view.search

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.view.common.OnScrollEnd
import jp.dosukoi.ui.viewmodel.search.SearchViewModel


@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val uiState by viewModel.searchUiState.collectAsState()
    val isTextError by viewModel.isError.observeAsState()
    val listState = rememberLazyListState()
    OnScrollEnd(lazyListState = listState, onAppearLastItem = viewModel::onScrollEnd)
    SearchComponent(
        uiState,
        isTextError,
        listState,
        viewModel::onSearchWordChanged,
        viewModel::onSearchButtonClick,
        viewModel::onRetryClick,
        viewModel::onSearchedItemClick
    )
}
