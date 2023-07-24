package com.csu_itc303_team1.adhdtaskmanager


import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.EditTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel
) {
    rewardViewModel.allRewards.observeAsState(listOf())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(TodoEvent.showDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todo"
                )
            }
        }
    ) { paddingValues ->

        if (state.showDialog){
            AddTodoDialog(
                state = state,
                onEvent = onEvent,
            )
        }


        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // TODO: This is where the task are filtered
                    // If you only want the completed task to show, then you can set
                    // sortType to SortType.BY_COMPLETED

                    SortType.values().forEach {sortType ->
                        Row (
                            modifier = Modifier.clickable { onEvent(TodoEvent.sortBy(sortType)) },
                            verticalAlignment = Alignment.CenterVertically
                                ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(TodoEvent.sortBy(sortType))
                                }
                            )
                            Text(text = sortType.name)
                        }
                    }

                }
            }

            items(state.todos){ todo ->
                if (todo.userID == state.userId){
                    TodoCard(todo = todo, todoState = state, onEvent = onEvent, rewardViewModel = rewardViewModel)
                }
            }
        }
    }
}