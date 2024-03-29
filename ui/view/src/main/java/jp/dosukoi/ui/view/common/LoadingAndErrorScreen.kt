package jp.dosukoi.ui.view.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import jp.dosukoi.ui.view.R
import jp.dosukoi.ui.viewmodel.common.LoadState

@Composable
fun <T> LoadingAndErrorScreen(
    state: LoadState<T>,
    loadedContent: @Composable (T) -> Unit,
    onRetryClick: () -> Unit
) {
    when (state) {
        LoadState.Loading -> LottieLoadingAnimation()
        is LoadState.Loaded -> loadedContent.invoke(state.data)
        is LoadState.Error -> LoadErrorContent(onRetryClick)
    }
}

@Composable
fun LottieLoadingAnimation() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_animation))
        LottieAnimation(modifier = Modifier.align(Alignment.Center), composition = composition)
    }
}

@Composable
private fun LoadErrorContent(
    onRetryClick: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.error_animation))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(120.dp)) {
            LottieAnimation(
                composition = composition,
                contentScale = ContentScale.Fit,
            )
        }
        Text(
            "Error!",
            modifier = Modifier.padding(top = 24.dp),
            style = TextStyle(color = error, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            "Opps, something went wrong\nPlease retry",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )
        Button(
            onClick = onRetryClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = error
            ),
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(text = "Try Again", style = TextStyle(color = white))
        }
    }
}

@Preview
@Composable
private fun PreviewLoadErrorContent() {
    LoadErrorContent {
    }
}
