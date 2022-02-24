package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
) {
    val myPageUiState by viewModel.myPageState.collectAsState()
    LoadingAndErrorScreen(
        state = myPageUiState,
        loadedContent = { data ->
            when (val state = data.userStatus) {
                is UserStatus.Authenticated -> MyPageComponent(
                    state.user,
                    data.repositoryList,
                    data.isRefreshing,
                    viewModel::onRefresh
                )
                UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent()
            }
        },
        onRetryClick = viewModel::onRetryClick
    )
}
