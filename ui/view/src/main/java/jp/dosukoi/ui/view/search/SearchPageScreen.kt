package jp.dosukoi.ui.view.search

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val searchPageState by viewModel.searchData.observeAsState()
    val searchWord by viewModel.searchWord.observeAsState()
    val hasMore by viewModel.hasMore.observeAsState()
    val isTextError by viewModel.isError.observeAsState()
    val listState = rememberLazyListState()
    val totalItemCount = listState.layoutInfo.totalItemsCount
    val visibleItemCount = listState.layoutInfo.visibleItemsInfo.size
    val firstItemIndex = listState.firstVisibleItemIndex
    if (firstItemIndex + visibleItemCount >= totalItemCount) {
        viewModel.onScrollEnd()
    }
    SearchComponent(
        searchPageState,
        searchWord,
        hasMore,
        isTextError,
        listState,
        viewModel::onSearchWordChanged,
        viewModel::onSearchButtonClick,
        viewModel::onRetryClick,
        viewModel::onSearchedItemClick
    )
}
