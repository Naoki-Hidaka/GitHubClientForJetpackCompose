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
import jp.dosukoi.ui.viewmodel.search.SearchViewModel
import jp.dosukoi.ui.viewmodel.top.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var myPageViewModelFactory: MyPageViewModel.Factory
    private val myPageViewModel: MyPageViewModel by viewModels {
        MyPageViewModel.Companion.Provider(myPageViewModelFactory, viewModel)
    }

    @Inject
    lateinit var searchViewModelFactory: SearchViewModel.Factory
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModel.Companion.Provider(searchViewModelFactory, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colors = appColors()
            ) {
                TopScreen(
                    searchViewModel,
                    myPageViewModel,
                )
            }
        }

        viewModel.onEvent.observe(this, ::handleEvent)
    }

    private fun handleEvent(event: MainViewModel.Event) {
        when (event) {
            MainViewModel.Event.CompleteGetAccessToken -> {
                myPageViewModel.onRetryClick()
            }
            is MainViewModel.Event.FailedGetAccessToken -> {
                showErrorToast(event.throwable)
            }
            is MainViewModel.Event.NavigateToChrome -> navigateChrome(event.url)
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
