package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel
import jp.dosukoi.ui.viewmodel.top.MainViewModel

@Composable
fun MyPageScreen(
    myPageViewModel: MyPageViewModel,
    mainViewModel: MainViewModel,
) {
    val loadState by myPageViewModel.loadState.observeAsState()
    val isRefreshing by myPageViewModel.isRefreshing.observeAsState()
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    is MyPageViewModel.UserStatus.Authenticated -> MyPageComponent(
                        it.item,
                        mainViewModel::onCardClick,
                        mainViewModel::onRepositoryItemClick,
                        isRefreshing,
                        myPageViewModel::onRefresh
                    )
                    MyPageViewModel.UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent(
                        mainViewModel::onLoginButtonClick
                    )
                }
            },
            onRetryClick = myPageViewModel::onRetryClick
        )
    }

}