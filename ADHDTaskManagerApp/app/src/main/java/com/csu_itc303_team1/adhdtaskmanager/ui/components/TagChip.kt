package com.csu_itc303_team1.adhdtaskmanager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme

@Composable
fun TagChip(
    tag: Tag,
    modifier: Modifier = Modifier
) {
    Text(
        text = tag.label,
        color = ADHDTaskManagerTheme.colors.tagText(tag.color),
        modifier = modifier
            .background(
                color = ADHDTaskManagerTheme.colors.tagBackground(tag.color),
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 6.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewTagChip() {
    ADHDTaskManagerTheme {
        Column {

        }
    }
}