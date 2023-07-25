package com.csu_itc303_team1.adhdtaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = NavyBlueDark,
    primaryVariant = BlueGrottoDark,
    secondary = BlueGreenDark,
    secondaryVariant = BabyBlueDark,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = BlueGrottoLight,
    primaryVariant = NavyBlueLight,
    secondary = BlueGreenLight,
    secondaryVariant = BabyBlueLight,
    background = BabyBlueLight,
    surface = Color.White,
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
            color = MaterialTheme.colors.primaryVariant,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = BackgroundDark,
            darkIcons = false
        )
        DarkColorPalette
    } else {
        systemUiController.setStatusBarColor(
            color = MaterialTheme.colors.primaryVariant,
            darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = BabyBlueLight,
            darkIcons = false
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}