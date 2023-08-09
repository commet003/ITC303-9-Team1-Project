package com.csu_itc303_team1.adhdtaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = NavyBlueLight,
    primaryContainer = BlueGrottoLight,
    secondary = BlueGreenLight,
    secondaryContainer = BabyBlueLight,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = DiologBoxBackgroundDark,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.White
)

private val LightColorPalette = lightColorScheme(
    primary = NavyBlueDark,
    primaryContainer = BlueGrottoDark,
    secondary = BlueGreenDark,
    secondaryContainer = BabyBlueDark,
    background = BackgroundLight,
    surface = DiologBoxBackgroundLight,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun ADHDTaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    val colors = if (darkTheme) {
        systemUiController.setStatusBarColor(
            color = NavyBlueLight,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = BackgroundDark,
            darkIcons = false
        )
        DarkColorPalette
    } else {
        systemUiController.setStatusBarColor(
            color = NavyBlueDark,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = BackgroundLight,
            darkIcons = true
        )
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}