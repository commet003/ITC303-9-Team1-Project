package com.csu_itc303_team1.adhdtaskmanager.screens.tasks

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.data.SortOrder

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterialApi
fun TasksScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.padding(10.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddClick(openScreen) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = modifier.padding(10.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        val tasks = viewModel.tasks.collectAsStateWithLifecycle(emptyList())

        Column(
            modifier = Modifier
            .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                filterTasksDropdown()
            }

            LazyColumn {
                items(tasks.value, key = { it.id }) { taskItem ->

                    TaskItem(
                        task = taskItem,
                        onActionClick = { action -> viewModel.onTaskActionClick(openScreen, taskItem, action) }
                    )
                }
            }
        }
    }

    LaunchedEffect(viewModel) { viewModel.loadTaskOptions() }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun filterTasksDropdown(

): SortOrder{

    var sortOrder = remember {SortOrder.NONE}
    var expanded by remember { mutableStateOf(false) }


    Column (
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // TODO: This is where the task are filtered
        // If you only want the completed task to show, then you can set
        // sortType to SortType.BY_COMPLETED
        ExposedDropdownMenuBox(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 10.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        )
        {
            IconButton(
                modifier = Modifier
                    .menuAnchor()
                    .align(Alignment.CenterHorizontally),
                onClick = { }) {
                Icon(
                    tint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Filled.List,
                    contentDescription = "Filter"
                )
            }

            ExposedDropdownMenu(
                modifier = Modifier
                    .width(150.dp)
                    .background(MaterialTheme.colorScheme.surface),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.NONE,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.NONE
                    },
                    text = { Text("None") }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.BY_DEADLINE,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.BY_DEADLINE
                    },
                    text = { Text("Deadline") }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.BY_PRIORITY,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.BY_PRIORITY
                    },
                    text = { Text("Priority") }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.BY_DEADLINE_AND_PRIORITY,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.BY_DEADLINE_AND_PRIORITY
                    },
                    text = { Text("Deadline and Priority") }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.BY_CATEGORY,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.BY_CATEGORY
                    },
                    text = { Text("Category") }
                )
                DropdownMenuItem(
                    modifier = Modifier.clip(MaterialTheme.shapes.medium),
                    colors = MenuItemColors(
                        textColor = MaterialTheme.colorScheme.onSurface,
                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    enabled = sortOrder != SortOrder.BY_DEADLINE_AND_CATEGORY,
                    onClick = {
                        expanded = false
                        sortOrder = SortOrder.BY_DEADLINE_AND_CATEGORY
                    },
                    text = { Text("Deadline and Category") }
                )
            }
        }
    }

    return sortOrder
}