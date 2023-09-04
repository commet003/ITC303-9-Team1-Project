package com.csu_itc303_team1.adhdtaskmanager.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun CardRibbon(colorInt: Int?, modifier: Modifier = Modifier) {
    val ribbonColor = if (colorInt != null) {
        Color(colorInt)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Spacer(
        modifier
            .width(18.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor),
    )
}