package jp.dosukoi.ui.view.myPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.dosukoi.ui.view.common.white

@Composable
fun UnAuthenticatedUserComponent(
    onLoginButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onLoginButtonClick,
            Modifier.background(white),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp)
        ) {
            Text("Login")
        }
    }
}