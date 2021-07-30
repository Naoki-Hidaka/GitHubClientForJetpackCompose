package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel,
    onLoginButtonClick: () -> Unit,
    onCardClick: (String) -> Unit
) {
    val loadState by viewModel.loadState.observeAsState()
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    is MyPageViewModel.UserStatus.Authenticated -> MyPageComponent(
                        it.user,
                        onCardClick
                    )
                    MyPageViewModel.UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent(
                        onLoginButtonClick
                    )
                }
            },
            onRetryClick = viewModel::onRetryClick
        )
    }

}