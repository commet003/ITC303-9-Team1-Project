/*package com.csu_itc303_team1.adhdtaskmanager.ui.todo_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.ui.dialogs.AddTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.ui.dialogs.EditTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen.RewardViewModel
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.TodoCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.SortType
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel,
    usersViewModel: UsersViewModel
) {
    rewardViewModel.allRewards.observeAsState(listOf())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    onEvent(TodoEvent.showDialog)
                }
            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.onPrimary,
                    imageVector = Icons.Default.Add,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .height(24.dp)
                        .width(24.dp),
                    contentDescription = "Add Todo"
                )
            }
        }
    ) { paddingValues ->
        var expanded by remember { mutableStateOf(false) }

        if (state.showDialog){
            AddTodoDialog(
                state = state,
                onEvent = onEvent,
            )
        }

        if (state.showEditTodoDialog){
            EditTodoDialog(
                state = state,
                onEvent = onEvent,
            )
        }

        Column (
            verticalArrangement = Arrangement.SpaceAround
        ){
            // TODO: This is where the task are filtered
            // If you only want the completed task to show, then you can set
            // sortType to SortType.BY_COMPLETED
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 10.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded}
            )
            {
                IconButton(
                    modifier = Modifier
                        .menuAnchor()
                        .align(Alignment.CenterHorizontally)
                    ,
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
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    SortType.values().forEach { sortType ->
                        DropdownMenuItem(
                            modifier = Modifier.clip(MaterialTheme.shapes.medium),
                            onClick = {
                                expanded = false
                                onEvent(TodoEvent.sortBy(sortType))
                            },
                            text = {
                                when (sortType.name) {
                                    "BY_PRIORITY" -> {
                                        Text(
                                            text = "By Priority",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    "BY_DATE_TIME" -> {
                                        Text(
                                            text = "By Date",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    "BY_COMPLETED" -> {
                                        Text(
                                            text = "By Completed",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                    "BY_NOT_COMPLETED" -> {
                                        Text(
                                            text = "By Not Completed",
                                            color = MaterialTheme.colorScheme.onSecondary
                                        )
                                    }
                                }
                            }
                        )
                    }
                }


            }

            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {

                items(state.todos) { todo ->
                    if (todo.userID == state.userId) {

                        TodoCard(
                            todo = todo,
                            todoState = state,
                            onEvent = onEvent,
                            index = state.todos.indexOf(todo),
                            rewardViewModel = rewardViewModel,
                            usersViewModel = usersViewModel
                        )
                    }
                }
            }

        }

    }
}

*/