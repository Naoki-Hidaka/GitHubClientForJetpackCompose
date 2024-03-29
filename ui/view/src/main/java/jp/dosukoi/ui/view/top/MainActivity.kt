package jp.dosukoi.ui.view.top

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import coil.compose.LocalImageLoader
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoi.ui.view.common.CompositionLocalProvider
import jp.dosukoi.ui.view.common.appColors
import jp.dosukoi.ui.viewmodel.myPage.MyPageViewModel
import jp.dosukoi.ui.viewmodel.search.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val myPageViewModel: MyPageViewModel by viewModels()

    private val searchViewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var compositionLocalProvider: CompositionLocalProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colors = appColors()
            ) {
                CompositionLocalProvider(
                    LocalImageLoader provides compositionLocalProvider.provideImageLoader()
                ) {
                    TopScreen(
                        searchViewModel,
                        myPageViewModel,
                    )
                }
            }
        }

        myPageViewModel.init()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.action
        if (action == null || action != Intent.ACTION_VIEW) {
            return
        }
        val uri = intent.data ?: return
        val code = uri.getQueryParameter("code")
        myPageViewModel.onGetCode(code)
    }
}
