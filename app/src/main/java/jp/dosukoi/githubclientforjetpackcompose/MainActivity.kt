package jp.dosukoi.githubclientforjetpackcompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoi.ui.view.common.appColors
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
                TopScreen(viewModel::onLoginButtonClick, viewModel::onCardClick)
            }
        }

        viewModel.onEvent.observe(this, ::handleEvent)
    }

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            MainViewModel.Event.ClickedLoginButton -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(MainViewModel.VERIFY_URL))
                startActivity(intent)
            }
            MainViewModel.Event.CompleteGetAccessToken -> {
                myPageViewModel.onRetryClick()
            }
            is MainViewModel.Event.FailedGetAccessToken -> {
                showErrorToast(event.throwable)
            }
            is MainViewModel.Event.ClickedCard -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                startActivity(intent)
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