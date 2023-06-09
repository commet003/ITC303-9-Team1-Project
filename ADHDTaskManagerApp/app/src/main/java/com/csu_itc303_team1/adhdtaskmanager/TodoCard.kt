package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.database.local.Reward
import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo


@Composable
fun TodoCard(todo: Todo, onEvent: (TodoEvent) -> Unit, rewardViewModel: RewardViewModel) {

    val searchResults by rewardViewModel.searchResults.observeAsState()
    rewardViewModel.findReward("Completed Task Reward")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    fontSize = 22.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(TodoEvent.showEditTodoDialog)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Todo"
                    )
                }
            }
            Row(
                modifier = Modifier.height(60.dp)
            ) {
                Text(text = todo.description)

                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        if (!todo.isCompleted) {

                            // If To-do is complete, mark to do as completed and update database
                            val updatedTodo = todo.copy(isCompleted = true)
                            onEvent(TodoEvent.markTodoAsCompleted(updatedTodo))

                            // get the Completed Reward Entity and update the times achieved.
                            val completedReward = searchResults?.get(0)
                            if (completedReward != null) {
                                completedReward.timesAchieved = completedReward.timesAchieved + 1
                            }
                            if (completedReward != null) {
                                rewardViewModel.updateReward(completedReward)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Mark as Completed"
                    )
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 5.dp),
                verticalAlignment = CenterVertically
            ) {
                Text(text = todo.priority.name)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueTime)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(TodoEvent.deleteTodo(todo))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Todo"

                    )
                }

            }
        }
    }
}