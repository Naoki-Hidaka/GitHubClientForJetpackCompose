package jp.dosukoi.ui.view.search

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import jp.dosukoi.ui.view.common.OnScrollEnd
import jp.dosukoi.ui.view.common.showErrorToast
import jp.dosukoi.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val uiState by viewModel.searchUiState.collectAsState()
    if (uiState.errors.isNotEmpty()) {
        val context = LocalContext.current
        val throwable = uiState.errors.first()
        context.showErrorToast(throwable)
        viewModel.onConsumeErrors(throwable)
    }
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
