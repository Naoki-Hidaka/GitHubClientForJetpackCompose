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
    val myPageState by viewModel.myPageState.collectAsState()
    LoadingAndErrorScreen(
        state = myPageState,
        loadedContent = { data ->
            when (val state = data.userStatus) {
                is UserStatus.Authenticated -> MyPageComponent(
                    state.user,
                    data.repositoryList,
                    viewModel::onCardClick,
                    viewModel::onRepositoryItemClick,
                    data.isRefreshing,
                    viewModel::onRefresh
                )
                UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent(
                    viewModel::onLoginButtonClick
                )
            }
        },
        onRetryClick = viewModel::onRetryClick
    )
}
