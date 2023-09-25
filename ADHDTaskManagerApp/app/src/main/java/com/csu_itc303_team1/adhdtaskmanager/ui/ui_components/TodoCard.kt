package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen.RewardViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager.AlarmItem
import com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager.AlarmSchedulerImpl
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SuppressLint("RememberReturnType")
@Composable
fun TodoCard(
    todo: Todo,
    onEvent: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel,
    usersViewModel: UsersViewModel,
    showToast: MutableState<Boolean>,
    alarmScheduler: AlarmSchedulerImpl
) {

    val showToastTrigger = remember { mutableStateOf(0) } // For triggering the toast
    val showLottieAnimation = remember { mutableStateOf(false) }
    var alarmItem: AlarmItem? = null


    LaunchedEffect(showToastTrigger.value) {
        if (showToastTrigger.value > 0) {
            showToast.value = true
            delay(3000) // 3 seconds
            showToast.value = false
        }
    }


    rewardViewModel.allRewards.observeAsState(listOf())
    val search by rewardViewModel.findReward("Completed Task Reward").observeAsState(listOf())


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
                    checked = todo.isCompleted,
                    onCheckedChange = {
                        // Prevent unchecking the checkbox
                        if (!todo.isCompleted) {
                            onEvent(TodoEvent.toggleCompleted(todo))

                            if (search.isNotEmpty()) {
                                val completedReward = search[0]
                                completedReward.timesAchieved = completedReward.timesAchieved + 1
                                rewardViewModel.updateReward(completedReward)
                                usersViewModel.completedTaskPoints()
                                if(todo.dueDate.isNotBlank() && todo.dueTime.isNotBlank()){
                                    alarmItem = AlarmItem(
                                        id = todo.id,
                                        time = LocalDateTime.parse(
                                            "${todo.dueDate} ${todo.dueTime}",
                                            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
                                        ).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                        title = todo.title,
                                        description = todo.description,
                                    )
                                    alarmItem?.let(alarmScheduler::cancel)
                                }
                                showToastTrigger.value += 1 // Increment to trigger the toast
                                onEvent(TodoEvent.ToggleLottieAnimation(true))
                                showLottieAnimation.value
                            } else {
                                println("Search Result on Todo Card is an empty list")
                            }
                        }
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !todo.isCompleted
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = todo.description,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 10.dp, top = 8.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = CenterVertically
            ) {
                Text(text = todo.priority.name, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(4.dp))
                if (todo.dueTime.isNotEmpty()) {
                        Text(text = todo.dueTime, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = "")
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        if(todo.dueDate.isNotBlank() && todo.dueTime.isNotBlank()) {
                            alarmItem = AlarmItem(
                                id = todo.id,
                                time = LocalDateTime.parse(
                                    "${todo.dueDate} ${todo.dueTime}",
                                    DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")
                                ).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                                title = todo.title,
                                description = todo.description,
                            )
                            alarmItem?.let(alarmScheduler::cancel)
                        }
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


    lottieLoaderAnimation(isVisible = showToast.value)
}



// Custom Toast Composable

@Composable
fun CustomToastMessage(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize() // Fills the entire screen
            .background(Color.Transparent)
    ) {
        if (isVisible) {
            // Display the toast box
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally) // Adjust width based on content
                    .wrapContentHeight(align = Alignment.CenterVertically) // Adjust height based on content
                    .background(
                        Color(0xFF11143E), // Using the RGBA color provided for the box background
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 24.dp) // Adjust the padding to make the box larger
                    .zIndex(1f) // Ensure the toast message is displayed below the Lottie animation
            ) {
                Text(
                    text = message,
                    color = Color.White, // Setting the text color to white
                    fontSize = 18.sp
                )
            }
        }
    }
}




@Composable
fun lottieLoaderAnimation(isVisible: Boolean) {
    if (isVisible) {
        val comositeResult: LottieCompositionResult = rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(com.csu_itc303_team1.adhdtaskmanager.R.raw.animation_llc3c1bg)
        )
        val progressionAnimation by animateLottieCompositionAsState(
            comositeResult.value,
            isPlaying = true,
            iterations = 1,
            speed = 1.0f
        )
        LottieAnimation(
            composition = comositeResult.value,
            progress = progressionAnimation,
            modifier = Modifier
                .fillMaxSize() // This makes sure the animation occupies the entire screen
                .zIndex(Float.MAX_VALUE) // This gives it the highest possible z-index
        )
    }
}