package com.csu_itc303_team1.adhdtaskmanager.data


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.ui.components.TodoCard
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen.TodoStatusGroup
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen.TodoStatusHeader
import com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen.TodosViewModel
import java.time.Clock
import java.time.Duration
import java.time.Instant

@Composable
fun TodoScreen(
    viewModel: TodosViewModel,
    onTodoClick: (todoId: Long) -> Unit,
    onAddTodoClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val statusGroups by viewModel.statusGroups.collectAsState(emptyMap())
    TodosContent(
        statusGroups = statusGroups,
        clock = viewModel.clock,
        onStatusClick = { viewModel.toggleStatusExpanded(it) },
        onStarClick = { viewModel.toggleTodoStarState(it) },
        onTodoClick = onTodoClick,
        onAddTodoClick = onAddTodoClick,
        onArchiveClick = onArchiveClick,
        onSettingsClick = onSettingsClick
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TodosContent(
    statusGroups: Map<TodoStatus, TodoStatusGroup>,
    clock: Clock,
    onStatusClick: (status: TodoStatus) -> Unit,
    onStarClick: (todoId: Long) -> Unit,
    onTodoClick: (todoId: Long) -> Unit,
    onAddTodoClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val systemBars = WindowInsets.systemBars
    var bottomBarHeight by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier
            .padding(top = systemBars.asPaddingValues().calculateTopPadding() + 20.dp)
            .windowInsetsPadding(systemBars),
        bottomBar = {
            TodosBottomBar(
                onArchiveClick = onArchiveClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier.onSizeChanged { size -> bottomBarHeight = size.height }
            )
        },
        floatingActionButton = {
            AddTodoButton(onClick = onAddTodoClick)
        },
        isFloatingActionButtonDocked = true
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = with(LocalDensity.current) { bottomBarHeight.toDp() + 4.dp }
            ),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            for ((status, group) in statusGroups) {
                stickyHeader(key = "header-${status.key}") {
                    TodoStatusHeader(
                        status = status,
                        count = group.summaries.size,
                        expanded = group.expanded,
                        onClick = { onStatusClick(status) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                items(items = group.summaries, key = { "todo-${it.id}" }) { summary ->
                    AnimatedVisibility(
                        visible = group.expanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                    ) {
                        TodoCard(
                            summary = summary,
                            clock = clock,
                            onStarClick = { onStarClick(summary.id) },
                            onClick = { onTodoClick(summary.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TodosBottomBar(
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val systemBars = WindowInsets.systemBars
    BottomAppBar(
        modifier = modifier.windowInsetsPadding(systemBars),
        cutoutShape = CircleShape
    ) {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.menu),
            )
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(onClick = onArchiveClick) {
                Text(text = stringResource(R.string.archive))
            }
            DropdownMenuItem(onClick = onSettingsClick) {
                Text(text = stringResource(R.string.settings))
            }
        }
    }
}

@Composable
private fun AddTodoButton(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.add_todo)
        )
    }
}

@Preview
@Composable
private fun PreviewTasksContent() {
    ADHDTaskManagerTheme {
        TodosContent(
            statusGroups = mapOf(
                TodoStatus.IN_PROGRESS to TodoStatusGroup(
                    expanded = true,
                    summaries = listOf(
                        TodoSummary(
                            id = 1L,
                            title = "Create default illustrations for event types",
                            status = TodoStatus.IN_PROGRESS,
                            dueAt = Instant.now() + Duration.ofHours(73),
                            orderInCategory = 1,
                            isArchived = false,
                            owner = User(
                                id = 1L,
                                username = "Daring Dove",
                                avatar = Avatar.USER_PROFILE_PICTURE
                            ),
                            tags = listOf(
                                Tag(id = 1L, label = "2.3 release", color = TagColor.BLUE),
                                Tag(id = 4L, label = "UI/UX", color = TagColor.PURPLE),
                            ),
                            starred = true,
                        )
                    )
                )
            ),
            clock = Clock.systemDefaultZone(),
            onStatusClick = {},
            onStarClick = {},
            onTodoClick = {},
            onAddTodoClick = {},
            onArchiveClick = {},
            onSettingsClick = {}
        )
    }
}