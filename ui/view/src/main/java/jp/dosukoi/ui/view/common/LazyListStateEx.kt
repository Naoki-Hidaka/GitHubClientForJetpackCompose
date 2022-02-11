package jp.dosukoi.ui.view.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.filter

@Composable
fun OnScrollEnd(
    lazyListState: LazyListState,
    onAppearLastItem: () -> Unit
) {
    val isReachedLast by remember(lazyListState) {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyListState.layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { isReachedLast }
            .filter { it }
            .collect {
                onAppearLastItem()
            }
    }
}
