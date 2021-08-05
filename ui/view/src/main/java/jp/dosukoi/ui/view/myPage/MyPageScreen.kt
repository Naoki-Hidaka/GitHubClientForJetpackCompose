package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel
import timber.log.Timber

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
) {
    val loadState by viewModel.loadState.observeAsState()
    val isRefreshing by viewModel.isRefreshing.observeAsState()
    val myPageState by viewModel.myPageState.observeAsState()
    Timber.d("debug: myPageState $myPageState")
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                myPageState?.let {
                    when (it.userStatus) {
                        is UserStatus.Authenticated -> MyPageComponent(
                            (it.userStatus as UserStatus.Authenticated).user,
                            it.repositoryList,
                            viewModel::onCardClick,
                            viewModel::onRepositoryItemClick,
                            isRefreshing,
                            viewModel::onRefresh
                        )
                        UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent(
                            viewModel::onLoginButtonClick
                        )
                    }
                }
            },
            onRetryClick = viewModel::onRetryClick
        )
    }

}