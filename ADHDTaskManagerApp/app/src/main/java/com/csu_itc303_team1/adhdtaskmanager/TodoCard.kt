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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen.RewardViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState


@SuppressLint("RememberReturnType")
@Composable
fun TodoCard(todo: Todo, todoState: TodoState, onEvent: (TodoEvent) -> Unit, index: Int, rewardViewModel: RewardViewModel) {

    rewardViewModel.allRewards.observeAsState(listOf())
    val search by rewardViewModel.findReward("Completed Task Reward").observeAsState(listOf())

    var hours = remember {
        mutableIntStateOf(0)
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
                        // rewardViewModel.findReward("Completed Task Reward")


                        if (search.isNotEmpty()) {
                            val completedReward = search[0]

                            if (!todo.isCompleted) {
                                completedReward.timesAchieved =
                                    completedReward.timesAchieved + 1
                                rewardViewModel.updateReward(completedReward)

                            }
                        } else {
                            println("Search Result on Todo Card is an empty list")
                        }

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
                if (todo.dueTime.isNotEmpty()) {
                    if (todo.dueTime.slice(0..1).toInt() > 12) {
                        hours.intValue = todo.dueTime.slice(0..1).toInt() - 12
                        Text(
                            text = "${hours.intValue}:${todo.dueTime.slice(3..4)} PM",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else if (todo.dueTime.slice(0..1).toInt() <= 12) {
                        Text(
                            text = "${todo.dueTime} AM",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(text = "")
                    }
                } else {
                    Text(text = "")
                }
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