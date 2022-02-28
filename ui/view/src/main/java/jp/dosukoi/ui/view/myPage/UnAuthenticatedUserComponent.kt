package jp.dosukoi.ui.view.myPage

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import jp.dosukoi.ui.view.common.white

private const val CLIENT_ID = "52b65f6025ea1e4264cd"
private const val VERIFY_URL =
    "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&scope=user repo"

@Composable
fun UnAuthenticatedUserComponent() {
    val context = LocalContext.current
    val intent = remember {
        Intent(Intent.ACTION_VIEW, Uri.parse(VERIFY_URL))
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { context.startActivity(intent) },
            Modifier.background(white),
            elevation = ButtonDefaults.elevation(defaultElevation = 8.dp)
        ) {
            Text("Login")
        }
    }
}
