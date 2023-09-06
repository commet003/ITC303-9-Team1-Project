package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.csu_itc303_team1.adhdtaskmanager.utils.ext.hasDueDate
import com.csu_itc303_team1.adhdtaskmanager.utils.ext.hasDueTime
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Category
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.delay




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun TodoItem(
    todo: Todo,
    showToast: MutableState<Boolean>,
    firestoreViewModel: FirestoreViewModel,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showToastTrigger = remember { mutableStateOf(0) } // For triggering the toast
    val showLottieAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(showToastTrigger.value) {
        if (showToastTrigger.value > 0) {
            showToast.value = true
            delay(3000) // 3 seconds
            showToast.value = false
        }
    }
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    var toastText by remember { mutableStateOf("") }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            when(it) {
                DismissValue.DismissedToEnd -> {
                    onEvent(TodoEvent.toggleCompleted(
                        todo = todo
                    ))
                    toastText = "Task Completed"
                    show = false
                    showToastTrigger.value += 1 // Increment to trigger the toast
                    onEvent(TodoEvent.ToggleLottieAnimation(true))
                    showLottieAnimation.value
                    true
                }
                DismissValue.DismissedToStart -> {
                    onEvent(TodoEvent.deleteTodo(
                        todo = todo
                    ))
                    toastText = "Task Deleted"
                    show = false
                    true
                }
                else -> {
                    false
                }
            }
        },
        positionalThreshold =  { 200.dp.value },
    )
    AnimatedVisibility(
        show,
        exit = fadeOut(spring())
    ) {
        SwipeToDismiss(
            state = dismissState,
            modifier = Modifier,
            background = {
                DismissBackground(dismissState)
            },
            dismissContent = {
                TodoCard(
                    todo = todo,
                    onEvent = onEvent,
                    modifier = modifier
                )
            },
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        )
    }
    lottieLoaderAnimation(isVisible = showToast.value)

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun TodoCard(
    todo: Todo,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier,
){
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Category.getCategoryByName(todo.category).color?.toArgb()?.let { Color(it) } ?: MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(74.dp)
            .clickable {
                onEvent(TodoEvent.toggleIsClicked(todo))
                onEvent(TodoEvent.showEditTodoDialog) },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            CardRibbon(colorInt = Category.getCategoryByName(todo.category).color?.toArgb())
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                , verticalArrangement = Arrangement.Center) {
                Text(
                    text = todo.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                RelativeDateText(todo = todo)
            }
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = when(todo.priority) {
                        1 -> "Low"
                        2 -> "Medium"
                        3 -> "High"
                        else -> "None"
                                               },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
    }
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

@Composable
internal fun RelativeDateText(todo: Todo) {
    Text(
        text = getDueDateAndTime(todo),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}


private fun getDueDateAndTime(todo: Todo): String {
    val stringBuilder = StringBuilder("")

    if (todo.hasDueDate()) {
        stringBuilder.append(todo.dueDate)
        stringBuilder.append(" ")
    }

    if (todo.hasDueTime()) {
        stringBuilder.append("at ")
        stringBuilder.append(todo.dueTime)
    }

    return stringBuilder.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color = when (dismissState.dismissDirection) {
        DismissDirection.StartToEnd -> Color(0xFF1DE9B6)
        DismissDirection.EndToStart -> Color(0xFFFF1744)
        null -> Color.Transparent
    }
    val direction = dismissState.dismissDirection

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (direction == DismissDirection.StartToEnd) {
            Icon(
                Icons.Default.CheckCircle,
                tint = Color.Black,
                contentDescription = "Complete Task"
            )
        }
        Spacer(modifier = Modifier)
        if (direction == DismissDirection.EndToStart) {
            Icon(
                Icons.Default.Delete,
                tint = Color.Black,

                contentDescription = "Delete Task"
            )
        }
    }
}


@Composable
internal fun CardRibbon(colorInt: Int?, modifier: Modifier = Modifier) {
    val ribbonColor = if (colorInt != null) {
        Color(colorInt)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Spacer(
        modifier
            .width(22.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor),
    )
}



