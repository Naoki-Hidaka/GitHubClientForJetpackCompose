package jp.dosukoi.ui.view.common

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppBarScaffold(
    title: String,
    content: @Composable () -> Unit,
    bottomNavigation: @Composable (() -> Unit)? = null,
    navigationIcon: ImageVector? = null,
    onNavigationClick: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = navigationIcon?.let {
                    {
                        IconButton(onClick = { onNavigationClick?.invoke() }) {
                            Icon(imageVector = it, contentDescription = null)
                        }
                    }
                }
            )
        },
        bottomBar = { bottomNavigation?.invoke() },
    ) {
        content.invoke()
    }
}

@Composable
fun BackAppBarScaffold(
    title: String,
    content: @Composable () -> Unit,
    bottomNavigation: @Composable (() -> Unit)? = null,
    onNavigationClick: (() -> Unit)
) {
    AppBarScaffold(
        title = title,
        content = content,
        bottomNavigation = bottomNavigation,
        navigationIcon = Icons.Filled.ArrowBack,
        onNavigationClick = onNavigationClick
    )
}

@Composable
fun CloseAppbarScaffold(
    title: String,
    content: @Composable () -> Unit,
    bottomNavigation: @Composable (() -> Unit)? = null,
    onNavigationClick: (() -> Unit)
) {
    AppBarScaffold(
        title = title,
        content = content,
        bottomNavigation = bottomNavigation,
        navigationIcon = Icons.Filled.Close,
        onNavigationClick = onNavigationClick
    )
}