package jp.dosukoi.ui.view.myPage

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyPageScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 30.dp, horizontal = 16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row {
               
            }
        }
    }
}