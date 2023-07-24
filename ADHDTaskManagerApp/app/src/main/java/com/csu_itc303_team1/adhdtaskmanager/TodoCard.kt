package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 10.dp)
                    .height(25.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    color = MaterialTheme.colors.onSecondary,
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
                        tint = MaterialTheme.colors.onSecondary,
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
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.onSecondary,
                        uncheckedColor = MaterialTheme.colors.onSecondary,
                        checkmarkColor = MaterialTheme.colors.secondary,
                        disabledColor = MaterialTheme.colors.onSecondary
                    )
                )
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    color = MaterialTheme.colors.onSecondary,
                    text = todo.description,
                    // Line through if completed
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row(
                modifier = Modifier.padding(start = 10.dp, top = 8.dp)
                    .fillMaxHeight(),
                verticalAlignment = CenterVertically
            ) {
                Text(text = todo.priority.name, color = MaterialTheme.colors.onSecondary)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate, color = MaterialTheme.colors.onSecondary)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueTime, color = MaterialTheme.colors.onSecondary)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEvent(TodoEvent.deleteTodo(todo))
                    }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.onSecondary,
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Todo"

                    )
                }

            }
        }
    }
    //   }

}