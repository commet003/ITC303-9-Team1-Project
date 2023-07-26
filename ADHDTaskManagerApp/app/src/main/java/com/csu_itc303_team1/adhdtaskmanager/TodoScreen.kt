package com.csu_itc303_team1.adhdtaskmanager


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.*
import androidx.compose.material.*
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.ui.theme.ADHDTaskManagerTheme
import kotlin.math.exp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.foundation.shape.CircleShape




@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel,
    navigateToPomodoroTimer: () -> Unit
) {
    rewardViewModel.allRewards.observeAsState(listOf())

    ADHDTaskManagerTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ADHD Task Manager") },
                )
            },
            bottomBar = {
                BottomAppBar(
                    cutoutShape = CircleShape
                ) {
                    IconButton(onClick = { navigateToPomodoroTimer() }) {
                        Icon(Icons.Filled.Timer, contentDescription = "Pomodoro Timer")
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    // Set color of button
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    onClick = {
                        onEvent(TodoEvent.showDialog)
                    }) {
                    Icon(
                        tint = MaterialTheme.colors.onPrimary,
                        imageVector = Icons.Default.Add,
                        modifier = Modifier.background(MaterialTheme.colors.primaryVariant),
                        contentDescription = "Add Todo"
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            var expanded by remember { mutableStateOf(false) }

            if (state.showDialog) {
                AddTodoDialog(
                    state = state,
                    onEvent = onEvent,
                )
            }


            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        // TODO: This is where the task are filtered
                        // If you only want the completed task to show, then you can set
                        // sortType to SortType.BY_COMPLETED
                        Column {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = Icons.Filled.List,
                                    contentDescription = "Filter"
                                )
                            }
                            DropdownMenu(
                                expanded = expanded,
                                modifier = Modifier.background(MaterialTheme.colors.background),
                                onDismissRequest = { expanded = false },
                            ) {
                                SortType.values().forEach { sortType ->
                                    Row(
                                        modifier = Modifier.clickable {
                                            onEvent(
                                                TodoEvent.sortBy(
                                                    sortType
                                                )
                                            )
                                        },
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        DropdownMenuItem(
                                            onClick = { onEvent(TodoEvent.sortBy(sortType)) }) {
                                            if (sortType.name == "BY_DATE") {
                                                Text(
                                                    text = "By Date",
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            } else if (sortType.name == "BY_PRIORITY") {
                                                Text(
                                                    text = "By Priority",
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            } else if (sortType.name == "BY_TIME") {
                                                Text(
                                                    text = "By Time",
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            } else if (sortType.name == "BY_COMPLETED") {
                                                Text(
                                                    text = "By Completed",
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            } else if (sortType.name == "BY_NOT_COMPLETED") {
                                                Text(
                                                    text = "By Not Completed",
                                                    color = MaterialTheme.colors.onBackground
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }
                items(state.todos) { todo ->
                    if (todo.userID == state.userId) {

                        TodoCard(
                            todo = todo,
                            todoState = state,
                            onEvent = onEvent,
                            index = state.todos.indexOf(todo),
                            rewardViewModel = rewardViewModel
                        )
                    }
                }
            }
        }
    }
}