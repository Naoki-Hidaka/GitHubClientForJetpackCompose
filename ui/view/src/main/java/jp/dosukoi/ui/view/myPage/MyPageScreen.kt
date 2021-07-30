package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
) {
    val loadState by viewModel.loadState.observeAsState()
    val isRefreshing by viewModel.isRefreshing.observeAsState()
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    is MyPageViewModel.UserStatus.Authenticated -> MyPageComponent(
                        it.item,
                        viewModel::onCardClick,
                        viewModel::onRepositoryItemClick,
                        isRefreshing,
                        viewModel::onRefresh
                    )
                    MyPageViewModel.UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent(
                        viewModel::onLoginButtonClick
                    )
                }
            },
            onRetryClick = viewModel::onRetryClick
        )
    }

}