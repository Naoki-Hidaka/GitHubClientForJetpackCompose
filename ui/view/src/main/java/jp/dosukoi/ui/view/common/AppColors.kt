package jp.dosukoi.ui.view.common

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

fun appColors(): Colors {
    return Colors(
        primary = primaryColor,
        primaryVariant = primaryColor,
        secondary = black,
        secondaryVariant = black,
        background = primaryColor,
        surface = white,
        error = error,
        onPrimary = black,
        onSecondary = white,
        onBackground = black,
        onSurface = white,
        onError = white,
        isLight = true
    )
}

val primaryColor = Color.White
val black = Color.Black
val white = Color.White
val gray = Color.Gray
val error = Color(0xFFFF6A56)