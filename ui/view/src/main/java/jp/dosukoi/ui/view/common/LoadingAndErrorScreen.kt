package jp.dosukoi.ui.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import jp.dosukoi.ui.viewmodel.common.LoadState

@Composable
fun <T> LoadingAndErrorScreen(
    state: LoadState<T>,
    loadedContent: @Composable (T) -> Unit,
    onRetryClick: () -> Unit
) {
    when (state) {
        is LoadState.Loading -> LoadingCircularProgressBar()
        is LoadState.Loaded -> loadedContent.invoke(state.value)
        is LoadState.Error -> LoadErrorContent(onRetryClick)
    }
}

@Composable
private fun LoadingCircularProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = black
        )
    }
}

@Composable
private fun LoadErrorContent(
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onRetryClick) {
            Text(text = "Retry", style = TextStyle(color = Color.Red))
        }
    }
}
