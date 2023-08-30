package com.csu_itc303_team1.adhdtaskmanager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagGroup(
    tags: List<Tag>,
    modifier: Modifier = Modifier,
    max: Int = Int.MAX_VALUE
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        for (tag in tags.take(max)) {
            TagChip(tag = tag)
        }
        if (tags.size > max) {
            Text(text = stringResource(R.string.more_tags, tags.size - max))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PreviewTagGroup() {
    ADHDTaskManagerTheme {

    }
}