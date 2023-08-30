package com.csu_itc303_team1.adhdtaskmanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = NavyBlueLight,
    primaryContainer = BlueGrottoLight,
    onPrimaryContainer = Color.White,
    secondary = BlueGreenLight,
    secondaryContainer = BabyBlueLight,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = DiologBoxBackgroundDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White,
    errorContainer = Color(0xFFCF0026),
    onErrorContainer = Color.White,
)

private val LightColorPalette = lightColorScheme(
    primary = NavyBlueDark,
    primaryContainer = BlueGrottoDark,
    onPrimaryContainer = Color.White,
    secondary = BlueGreenDark,
    onSecondary = Color.White,
    secondaryContainer = BabyBlueDark,
    background = BackgroundLight,
    surface = DiologBoxBackgroundLight,
    onPrimary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    errorContainer = Color(0xFF8D001A),
    onErrorContainer = Color.White,
)


object ADHDTaskManagerTheme {

    /**
     * Extra theme colors.
     */
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

@Composable
fun ADHDTaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}