package com.csu_itc303_team1.adhdtaskmanager.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.data.Avatar
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.data.TagColor
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.TodoSummary
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import com.csu_itc303_team1.adhdtaskmanager.utils.DateTimeUtils
import java.time.Clock
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoCard(
    summary: TodoSummary,
    clock: Clock,
    onStarClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row {
            Spacer(modifier = Modifier.width(8.dp))
            // The star icon.
            StarIconButton(
                onClick = onStarClick,
                filled = summary.starred,
                contentDescription = "",
                modifier = Modifier.padding(top = 8.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.padding(top = 12.dp, end = 12.dp, bottom = 12.dp)) {
                // The title.
                Text(
                    text = summary.title,
                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                // The owner.
                Spacer(modifier = Modifier.height(2.dp))
                // The due date.
                TodoSummaryDueAt(dueAt = summary.dueAt, clock = clock)
                Spacer(modifier = Modifier.height(4.dp))
                // The tags.
                TagGroup(
                    tags = summary.tags,
                    max = 3
                )
            }
        }
    }
}


@Composable
private fun TodoSummaryDueAt(dueAt: Instant, clock: Clock) {
    val resources = LocalContext.current.resources
    Text(
        text = DateTimeUtils.durationMessageOrDueDate(resources, dueAt, clock),
    )
}

@Preview
@Composable
private fun PreviewTodoCard() {
    ADHDTaskManagerTheme {
        TodoCard(
            summary = TodoSummary(
                id = 1L,
                title = "Create default illustrations for event types",
                status = TodoStatus.IN_PROGRESS,
                dueAt = Instant.now() + Duration.ofHours(73),
                orderInCategory = 1,
                isArchived = false,
                owner = User(id = 1L, username = "username", avatar = Avatar.USER_PROFILE_PICTURE),
                tags = listOf(
                    Tag(id = 1L, label = "2.3 release", color = TagColor.BLUE),
                    Tag(id = 4L, label = "UI/UX", color = TagColor.PURPLE),
                ),
                starred = true,
            ),
            clock = Clock.systemDefaultZone(),
            onStarClick = {},
            onClick = {},
        )
    }
}