package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import jp.dosukoi.githubclient.domain.entity.myPage.UserStatus
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.view.common.showErrorToast
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
) {
    val myPageUiState by viewModel.myPageState.collectAsState()
    if (myPageUiState.errors.isNotEmpty()) {
        val context = LocalContext.current
        val throwable = myPageUiState.errors.first()
        context.showErrorToast(throwable)
        viewModel.onConsumeErrors(throwable)
    }
    LoadingAndErrorScreen(
        state = myPageUiState.screenState,
        loadedContent = { data ->
            when (val state = data.userStatus) {
                is UserStatus.Authenticated -> MyPageComponent(
                    state.user,
                    data.repositoryList,
                    myPageUiState.isRefreshing,
                    viewModel::onRefresh
                )
                UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent()
            }
        },
        onRetryClick = viewModel::onRetryClick
    )
}
