package jp.dosukoi.ui.view.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
    hasMore: Boolean?,
    isTextError: Boolean?,
    listState: LazyListState,
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
            isTextError = isTextError,
            onValueChanged = onValueChanged,
            onSearchButtonClick = onSearchButtonClick
        )
        SearchList(
            loadState = loadState,
            hasMore = hasMore,
            listState = listState,
            onRetryClick = onRetryClick,
            onItemClick = onItemClick
        )
    }
}

@Composable
fun SearchTextField(
    searchText: String,
    isTextError: Boolean?,
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
        },
        isError = isTextError ?: false
    )
}

@Composable
fun SearchList(
    loadState: LoadState<SearchViewModel.State>?,
    hasMore: Boolean?,
    listState: LazyListState,
    onRetryClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    SearchViewModel.State.Initialized -> SearchInitialComponent()
                    SearchViewModel.State.Empty -> SearchedEmptyComponent()
                    is SearchViewModel.State.Data ->
                        SearchedListComponent(it.repositoryList, hasMore, listState, onItemClick)
                }
            },
            onRetryClick = onRetryClick
        )
    }
}

@Composable
fun SearchedListComponent(
    repositoryList: List<Repository>,
    hasMore: Boolean?,
    listState: LazyListState,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp),
        state = listState
    ) {
        repositoryList.forEachIndexed { index, repository ->
            item {
                RepositoryItem(
                    repository,
                    index == repositoryList.size - 1 && hasMore == false,
                    onItemClick
                )
            }
        }
        if (hasMore == true) item { LoadingFooter() }
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

@Composable
fun LoadingFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(10.dp),
            color = black
        )
    }
}

@Preview
@Composable
fun PreviewSearchTextField() {
    SearchTextField(
        searchText = "hoge",
        isTextError = true,
        onValueChanged = {},
        onSearchButtonClick = {})
}

@Preview
@Composable
fun PreviewSearchedEmptyComponent() {
    SearchedEmptyComponent()
}