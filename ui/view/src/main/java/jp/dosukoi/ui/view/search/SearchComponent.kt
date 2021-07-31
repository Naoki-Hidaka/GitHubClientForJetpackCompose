package jp.dosukoi.ui.view.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.view.common.black
import jp.dosukoi.ui.view.common.white
import jp.dosukoi.ui.view.myPage.RepositoryItem
import jp.dosukoi.ui.viewmodel.common.LoadState
import jp.dosukoi.ui.viewmodel.search.SearchViewModel

@Composable
fun SearchComponent(
    loadState: LoadState<SearchViewModel.State>?,
    searchText: String?,
    onValueChanged: (String) -> Unit,
    onSearchButtonClick: () -> Unit,
    onRetryClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchTextField(
            searchText = searchText ?: "",
            onValueChanged = onValueChanged,
            onSearchButtonClick = onSearchButtonClick
        )
        SearchList(
            loadState = loadState,
            onRetryClick = onRetryClick,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun SearchTextField(
    searchText: String,
    onValueChanged: (String) -> Unit,
    onSearchButtonClick: () -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp),
        value = searchText,
        onValueChange = onValueChanged,
        colors = TextFieldDefaults.textFieldColors(
            textColor = black,
            backgroundColor = white,
            cursorColor = black.copy(alpha = 0.42f),
            trailingIconColor = black,
            focusedIndicatorColor = black.copy(alpha = 0.42f)
        ),
        trailingIcon = {
            IconButton(
                onClick = onSearchButtonClick,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            )
        },
        placeholder = {
            Text(text = "Search")
        }
    )
}

@Composable
fun SearchList(
    loadState: LoadState<SearchViewModel.State>?,
    onRetryClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    SearchViewModel.State.Initialized -> SearchInitialComponent()
                    SearchViewModel.State.Empty -> SearchedEmptyComponent()
                    is SearchViewModel.State.Data ->
                        SearchedListComponent(it.repositoryList, onItemClick)
                }
            },
            onRetryClick = onRetryClick
        )
    }
}

@Composable
fun SearchedListComponent(
    repositoryList: List<Repository>,
    onItemClick: (String) -> Unit
) {
    val scrollState = rememberLazyListState()
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp),
        state = scrollState
    ) {
        repositoryList.forEachIndexed { index, repository ->
            item {
                RepositoryItem(
                    repository,
                    index == repositoryList.size - 1,
                    onItemClick
                )
            }
        }
    }
}

@Composable
fun SearchInitialComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please enter a search word to search the repository",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchedEmptyComponent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oops, Repository is not found.\n" +
                    "Please change search word and retry again.",
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun PreviewSearchTextField() {
    SearchTextField(searchText = "hoge", onValueChanged = {}, onSearchButtonClick = {})
}

@Preview
@Composable
fun PreviewSearchedEmptyComponent() {
    SearchedEmptyComponent()
}