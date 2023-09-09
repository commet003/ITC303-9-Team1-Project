package com.csu_itc303_team1.adhdtaskmanager.screens.tasks


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.common.ext.hasDueDate
import com.csu_itc303_team1.adhdtaskmanager.common.ext.hasDueTime
import com.csu_itc303_team1.adhdtaskmanager.model.Category
import com.csu_itc303_team1.adhdtaskmanager.model.Priority
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun TaskItem(
    task: Task,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Log.d("TaskItem", "$task")
    val context = LocalContext.current
    var show by remember { mutableStateOf(true) }
    var toastText by remember { mutableStateOf("") }
    var actionOption by remember { mutableStateOf("") }
    val dismissState = rememberDismissState(
        confirmValueChange = {
            when(it) {
                DismissValue.DismissedToEnd -> {
                    actionOption = "Complete task"
                    toastText = "Task Completed"
                    show = false
                    true
                }
                DismissValue.DismissedToStart -> {
                    actionOption = "Delete task"
                    toastText = "Task Deleted"
                    show = false
                    true
                }
                else -> {
                    false
                }
            }
        },
        positionalThreshold =  { 150.dp.value },
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
                TaskCard(
                    task = task,
                    onActionClick = onActionClick,
                    modifier = modifier
                )
            },
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        )
    }

    LaunchedEffect(show) {
        if (!show) {
            delay(800)
            onActionClick(actionOption)
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        }
    }
}


@Composable
fun TaskCard(
    task: Task,
    onActionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    Card(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Category.getCategoryByName(task.category).color?.toArgb()?.let { Color(it) } ?: MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(74.dp)
            .clickable { onActionClick("Edit task") },
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        Log.d("isDarkMode", isSystemInDarkTheme().toString())
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            CardRibbon(colorInt = Category.getCategoryByName(task.category).color?.toArgb())
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                , verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                RelativeDateText(task = task)
            }
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier.fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surface),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text =  Priority.getByValue(task.priority).name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    )
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
internal fun RelativeDateText(task: Task) {
    Text(
        text = getDueDateAndTime(task),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}


private fun getDueDateAndTime(task: Task): String {
    val stringBuilder = StringBuilder("")

    if (task.hasDueDate()) {
        stringBuilder.append(task.dueDate)
        stringBuilder.append(" ")
    }

    if (task.hasDueTime()) {
        stringBuilder.append("at ")
        stringBuilder.append(task.dueTime)
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