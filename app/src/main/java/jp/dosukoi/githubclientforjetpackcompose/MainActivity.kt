package jp.dosukoi.githubclientforjetpackcompose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoi.ui.view.common.appColors
import jp.dosukoi.ui.view.common.navigateChrome
import jp.dosukoi.ui.view.common.showErrorToast
import jp.dosukoi.ui.view.top.TopScreen
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel
import jp.dosukoi.ui.viewmodel.top.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colors = appColors()
            ) {
                TopScreen(
                    myPageViewModel,
                    viewModel::onLoginButtonClick,
                    viewModel::onCardClick,
                    viewModel::onRepositoryItemClick
                )
            }
        }

        viewModel.onEvent.observe(this, ::handleEvent)
    }

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            MainViewModel.Event.ClickedLoginButton -> {
                navigateChrome(MainViewModel.VERIFY_URL)
            }
            MainViewModel.Event.CompleteGetAccessToken -> {
                myPageViewModel.onRefresh()
            }
            is MainViewModel.Event.FailedGetAccessToken -> {
                showErrorToast(event.throwable)
            }
            is MainViewModel.Event.ClickedCard -> {
                navigateChrome(event.url)
            }
            is MainViewModel.Event.ClickedRepositoryItem -> {
                navigateChrome(event.url)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action
        if (action == null || action != Intent.ACTION_VIEW) {
            return
        }
        val uri = intent.data ?: return
        val code = uri.getQueryParameter("code")
        viewModel.onGetCode(code)
    }
}