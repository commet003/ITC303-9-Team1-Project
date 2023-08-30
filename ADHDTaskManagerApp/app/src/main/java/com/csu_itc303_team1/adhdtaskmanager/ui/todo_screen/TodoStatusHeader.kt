package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun TodoStatusHeader(
    status: TodoStatus,
    count: Int,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    stringResource(
        if (expanded) {
            R.string.collapse_todos
        } else {
            R.string.expand_todos
        }
    )
    Surface(
        onClick = onClick,
        color = MaterialTheme.colors.primarySurface
    ) {
        Row(
            modifier = modifier
                .heightIn(min = 64.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (expanded) {
                    Icons.Default.KeyboardArrowDown
                } else {
                    Icons.Default.KeyboardArrowUp
                },
                contentDescription = null
            )
            Text(
                text = stringResource(
                    R.string.header_label_with_count,
                    stringResource(status.stringResId),
                    count
                ),
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTodoStatusHeader() {
    Column {
        for (darkTheme in listOf(false, true)) {
            ADHDTaskManagerTheme(darkTheme = darkTheme) {
                TodoStatusHeader(
                    status = TodoStatus.NOT_STARTED,
                    count = 3,
                    expanded = false,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}