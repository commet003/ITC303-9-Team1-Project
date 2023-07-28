package com.csu_itc303_team1.adhdtaskmanager

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("RememberReturnType")
@Composable
fun TodoCard(todo: Todo, todoState: TodoState, onEvent: (TodoEvent) -> Unit, index: Int, rewardViewModel: RewardViewModel) {


    val searchResults by rewardViewModel.searchResults.observeAsState()
    rewardViewModel.findReward("Completed Task Reward")

    // show the edit todo dialog if showEditTodoDialog is true
    if (todoState.showEditTodoDialog){
        EditTodoDialog(
            todoState,
            onEvent
        )
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 5.dp)
                    .height(25.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 22.sp,
                    // Line through if completed
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(TodoEvent.toggleIsClicked(todo))
                        onEvent(TodoEvent.showEditTodoDialog)
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Todo"
                    )
                }
            }
            Row(
                modifier = Modifier.height(60.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Checkbox(

                    checked = todo.isCompleted, onCheckedChange = {
                        onEvent(TodoEvent.toggleCompleted(todo))

                        // get the Completed Reward Entity and update the times achieved.

                        //val completedReward = searchResults?.get(1)

                        /*if (!todo.isCompleted) {
                            if (completedReward != null) {

                                completedReward.timesAchieved =
                                    completedReward.timesAchieved + 1
                            }
                            if (completedReward != null) {
                                rewardViewModel.updateReward(completedReward)
                            }

                        }*/
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = todo.description,
                    // Line through if completed
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 8.dp)
                    .fillMaxHeight(),
                verticalAlignment = CenterVertically
            ) {
                Text(text = todo.priority.name, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueTime, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(TodoEvent.deleteTodo(todo))
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Todo"

                    )
                }

            }
        }
    }
    //   }

}