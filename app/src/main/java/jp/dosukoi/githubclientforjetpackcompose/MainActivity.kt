package jp.dosukoi.githubclientforjetpackcompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import jp.dosukoi.ui.view.common.appColors
import jp.dosukoi.ui.view.top.TopScreen

class MainActivity : AppCompatActivity() {
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