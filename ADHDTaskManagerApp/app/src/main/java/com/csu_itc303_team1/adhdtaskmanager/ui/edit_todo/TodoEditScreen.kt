package com.csu_itc303_team1.adhdtaskmanager.ui.edit_todo

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.StarPurple500
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.R


@Composable
fun TodoEditScreen(
    viewModel: TodoEditViewModel,
    onConfirmEditClick: () -> Unit,
    onBackClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val status by viewModel.status.collectAsState()
    EditTodoContent(
        viewModel = viewModel,
        onStatusClick = { viewModel.updateState(status) },
        onBackClick = onBackClick,
        onConfirmClick = onConfirmEditClick,
        onArchiveClick = onArchiveClick,
        onSettingsClick = onSettingsClick,
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun EditTodoContent(
    viewModel: TodoEditViewModel,
    onStatusClick: () -> Unit,
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onSettingsClick: () -> Unit,
    ){
    val systemBars = WindowInsets.systemBars
    var bottomBarHeight by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier
            .padding(
                top = systemBars
                    .asPaddingValues()
                    .calculateTopPadding() + 20.dp
            )
            .windowInsetsPadding(systemBars),
        bottomBar = {
            EditTodoBottomBar(
                onArchiveClick = onArchiveClick,
                onSettingsClick = onSettingsClick,
                modifier = Modifier.onSizeChanged { size -> bottomBarHeight = size.height }
            )
        },
        floatingActionButton = {
            EditTodoButton(onClick = onConfirmClick)
        },
        isFloatingActionButtonDocked = true
    ) {
        Column {
            TextField(
                value = viewModel.title.value,
                onValueChange = viewModel.title::value::set,
                singleLine = true,
            )

            TextField(
                value = viewModel.description.value,
                onValueChange = viewModel.description::value::set,
                singleLine = false,
            )

            Text(text = viewModel.status.value.toString())

            Row{
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Due Date",
                )
                Text(text = "Due Date:")
            }
            Text(text = viewModel.dueAt.value.toString())

            Row {
Icon(
                    imageVector = Icons.Default.StarPurple500,
                    contentDescription = "Tags",
                )
                Text(text = "Tags:")
            }
            Text(text = viewModel.tags.value.toString())

        }
    }
}

@Composable
private fun EditTodoBottomBar(
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
private fun EditTodoButton(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.edit_todo)
        )
    }
}

