package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.EditTodoDialog


@Composable
fun TodoCard(todo: Todo, todoState: TodoState, onEvent: (TodoEvent) -> Unit, rewardViewModel: RewardViewModel) {

    val searchResults by rewardViewModel.searchResults.observeAsState()
    rewardViewModel.findReward("Completed Task Reward")


    //   Scaffold() {

    if (todoState.showEditTodoDialog) {
        EditTodoDialog(
            todo = todo,
            state = todoState,
            onEvent = onEvent,
        )
    }

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
                    // Line through if completed
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
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
                modifier = Modifier.height(60.dp),
                verticalAlignment = CenterVertically
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
                    })
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = todo.description,
                    // Line through if completed
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
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
    //   }

}