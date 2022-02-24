package jp.dosukoi.ui.view.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.view.common.black
import jp.dosukoi.ui.view.common.white
import jp.dosukoi.ui.view.myPage.RepositoryItem
import jp.dosukoi.ui.viewmodel.search.SearchState
import jp.dosukoi.ui.viewmodel.search.SearchUiState

@Composable
fun SearchComponent(
    uiState: SearchUiState,
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
            searchText = uiState.searchWord,
            isTextError = isTextError,
            onValueChanged = onValueChanged,
            onSearchButtonClick = onSearchButtonClick
        )
        SearchList(
            uiState = uiState,
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
    val focusManager = LocalFocusManager.current
    val onSearch = {
        onSearchButtonClick.invoke()
        focusManager.clearFocus()
    }
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
                onClick = onSearch,
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
        isError = isTextError ?: false,
        maxLines = 1,
        keyboardActions = KeyboardActions(onSearch = { onSearch.invoke() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
    )
}

@Composable
fun SearchList(
    uiState: SearchUiState,
    listState: LazyListState,
    onRetryClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    LoadingAndErrorScreen(
        state = uiState.searchState,
        loadedContent = {
            when (it) {
                SearchState.Initialized -> SearchInitialComponent()
                SearchState.Empty -> SearchedEmptyComponent()
                is SearchState.Data ->
                    SearchedListComponent(
                        it.repositoryList,
                        it.hasMore,
                        listState,
                        onItemClick
                    )
            }
        },
        onRetryClick = onRetryClick
    )
}

@Composable
fun SearchedListComponent(
    repositoryList: List<Repository>,
    hasMore: Boolean,
    listState: LazyListState,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp),
        state = listState
    ) {
        itemsIndexed(repositoryList) { index, repository ->
            RepositoryItem(
                repository = repository,
                isLastItem = index == repositoryList.size && !hasMore,
                onRepositoryItemClick = onItemClick
            )
        }
        if (hasMore) item { LoadingFooter() }
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
