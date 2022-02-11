package jp.dosukoi.ui.viewmodel.myPage

import jp.dosukoi.data.entity.myPage.Repository
import jp.dosukoi.data.entity.myPage.UserStatus

data class MyPageUiState(
    val userStatus: UserStatus,
    val repositoryList: List<Repository>,
    val isRefreshing: Boolean
)
