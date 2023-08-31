package com.csu_itc303_team1.adhdtaskmanager.ui.todo_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import com.csu_itc303_team1.adhdtaskmanager.R.*

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TodoDetailScreen(
    viewModel: TodoDetailViewModel,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    TodoDetailContent(
        viewModel = viewModel,
        onStatusClick = viewModel::toggleTodoStarState,
        onBackClick = onBackClick,
        onEditClick = {onEditClick()},
        onArchiveClick = onArchiveClick,
        onSettingsClick = onSettingsClick,
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun TodoDetailContent(
    viewModel: TodoDetailViewModel,
    onStatusClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditClick: (todoId: Long) -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
){
    var bottomBarHeight by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            TodoDetailBottomBar(
                onArchiveClick = onArchiveClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier.onSizeChanged { size -> bottomBarHeight = size.height }
            )
        },
        floatingActionButton = {
            TodoDetailButton { onEditClick(viewModel.todoId.value) }
        },
        isFloatingActionButtonDocked = true
    ) {
        Column {
            Text(text = "${viewModel.detail.value?.title}")

            Text(text = "${viewModel.detail.value?.description}")

            Text(text = "${viewModel.detail.value?.status}")

            Row{
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Due Date",
                )
                Text(text = "Due Date:")
            }
            Text(text = "${viewModel.detail.value?.dueAt}")

            Row {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Tags",
                )
                Text(text = "Tags:")
            }
            Text(text = "${viewModel.detail.value?.tags}")

        }
    }
}

@Composable
private fun TodoDetailBottomBar(
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
                contentDescription = stringResource(string.menu),
            )
        }
        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(onClick = onArchiveClick) {
                Text(text = stringResource(string.archive))
            }
            DropdownMenuItem(onClick = onSettingsClick) {
                Text(text = stringResource(string.settings))
            }
        }
    }
}

@Composable
private fun TodoDetailButton(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Todo"
        )
    }
}