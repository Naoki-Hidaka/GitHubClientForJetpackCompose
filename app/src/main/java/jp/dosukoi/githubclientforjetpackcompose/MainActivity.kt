package jp.dosukoi.githubclientforjetpackcompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import jp.dosukoi.ui.view.common.appColors
import jp.dosukoi.ui.view.top.TopScreen
import jp.dosukoi.ui.viewmodel.top.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colors = appColors()
            ) {
                TopScreen()
            }
        }
    }
}