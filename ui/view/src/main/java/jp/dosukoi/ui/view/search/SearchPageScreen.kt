package jp.dosukoi.ui.view.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel
) {
    val loadState by viewModel.loadState.observeAsState()
    val searchWord by viewModel.searchWord.observeAsState()
    SearchComponent(
        loadState,
        searchWord,
        viewModel::onSearchWordChanged,
        viewModel::onSearchButtonClick,
        viewModel::onRetryClick,
        viewModel::onSearchedItemClick
    )
}