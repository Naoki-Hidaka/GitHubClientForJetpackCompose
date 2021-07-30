package jp.dosukoi.ui.view.myPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.dosukoi.ui.view.common.LoadingAndErrorScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel

@Composable
fun MyPageScreen(
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val loadState by viewModel.loadState.observeAsState()
    loadState?.let {
        LoadingAndErrorScreen(
            state = it,
            loadedContent = {
                when (it) {
                    is MyPageViewModel.UserStatus.Authenticated -> MyPageComponent(it.user)
                    MyPageViewModel.UserStatus.UnAuthenticated -> UnAuthenticatedUserComponent()
                }
            },
            onRetryClick = viewModel::onRetryClick
        )
    }

}

@Preview
@Composable
fun PreviewMyPageScreen() {
    MyPageScreen()
}