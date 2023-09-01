package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csu_itc303_team1.adhdtaskmanager.TASK_COMPLETED_REWARD
import com.csu_itc303_team1.adhdtaskmanager.TASK_COMPLETED_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("RememberReturnType")
@Composable
fun TodoCard(
    todo: Todo,
    currentUser: AuthUiClient,
    firestoreViewModel: FirestoreViewModel,
    onEvent: (TodoEvent) -> Unit,
    showToast: MutableState<Boolean>
) {

    val showToastTrigger = remember { mutableIntStateOf(0) } // For triggering the toast
    val showLottieAnimation = remember { mutableStateOf(false) }


    LaunchedEffect(showToastTrigger.intValue) {
        if (showToastTrigger.intValue > 0) {
            showToast.value = true
            delay(3000) // 3 seconds
            showToast.value = false
            showToastTrigger.intValue = 0 // Reset the trigger
        }
    }


    val hours = remember {
        mutableIntStateOf(0)
    }

    var offsetX by remember { mutableStateOf(0f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(160.dp)
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .clickable {
                onEvent(TodoEvent.toggleIsClicked(todo))
                onEvent(TodoEvent.showEditTodoDialog)
            }
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { dx ->
                    offsetX += dx
                    if (dx > 150.dp.value) {
                        onEvent(TodoEvent.deleteTodo(todo))
                    } else if (dx < -150.dp.value) {
                        onEvent(TodoEvent.toggleCompleted(todo))
                        if (!todo.isCompleted) {
                            firestoreViewModel.updateUserRewardsPoints(
                                TASK_COMPLETED_REWARD,
                                currentUser.getSignedInUser()!!.userID.toString()
                            )
                            firestoreViewModel.viewModelScope.launch {
                                firestoreViewModel.incrementRewardCount(
                                    currentUser.getSignedInUser()!!.userID.toString(),
                                    TASK_COMPLETED_REWARD_NAME
                                )
                            }
                            showToastTrigger.intValue += 1 // Increment to trigger the toast
                            onEvent(TodoEvent.ToggleLottieAnimation(true))
                            showLottieAnimation.value
                        }
                    }
                },
                onDragStopped = {
                    offsetX = 0f
                }
            ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 10.dp)
                    .height(25.dp),
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 22.sp,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row(
                modifier = Modifier.height(60.dp)
                    .padding(start = 15.dp, top = 8.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    text = todo.description,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            }
            Row(
                modifier = Modifier
                    .padding(start = 15.dp, top = 8.dp)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = CenterVertically
            ) {
                Text(text = todo.priority.name, color = MaterialTheme.colorScheme.onTertiaryContainer)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate, color = MaterialTheme.colorScheme.onTertiaryContainer)
                Spacer(Modifier.width(4.dp))
                if (todo.dueTime.isNotEmpty()) {
                    if (todo.dueTime.slice(0..1).toInt() > 12) {
                        hours.intValue = todo.dueTime.slice(0..1).toInt() - 12
                        Text(
                            text = "${hours.intValue}:${todo.dueTime.slice(3..4)} PM",
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    } else if (todo.dueTime.slice(0..1).toInt() <= 12) {
                        Text(
                            text = "${todo.dueTime} AM",
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    } else {
                        Text(text = "")
                    }
                } else {
                    Text(text = "")
                }
            }
        }
    }


    LottieLoaderAnimation(isVisible = showToast.value)
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
                    .wrapContentHeight(align = CenterVertically) // Adjust height based on content
                    .background(
                        Color(0xFF11143E), // Using the RGBA color you provided for the box background
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    ) // Adjust the padding to make the box larger
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
fun LottieLoaderAnimation(isVisible: Boolean) {
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