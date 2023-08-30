package com.csu_itc303_team1.adhdtaskmanager.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme

@Composable
fun StarIconButton(
    onClick: () -> Unit,
    filled: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Image(
            imageVector = if (filled) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(ADHDTaskManagerTheme.colors.star),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStarIconButtons() {
    ADHDTaskManagerTheme {
        Column {
            StarIconButton(
                onClick = {},
                filled = true,
                contentDescription = null,
            )
            StarIconButton(
                onClick = {},
                filled = false,
                contentDescription = null,
            )
        }
    }
}
