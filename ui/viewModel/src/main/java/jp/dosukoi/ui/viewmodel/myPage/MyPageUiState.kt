package jp.dosukoi.ui.viewmodel.myPage

import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.UserStatus
import jp.dosukoi.ui.viewmodel.common.LoadState

data class MyPageUiState(
    val screenState: LoadState<MyPageScreenState> = LoadState.Loading,
    val isRefreshing: Boolean = false,
    val errors: List<Throwable> = emptyList()
)

data class MyPageScreenState(
    val userStatus: UserStatus,
    val repositoryList: List<Repository>
)
