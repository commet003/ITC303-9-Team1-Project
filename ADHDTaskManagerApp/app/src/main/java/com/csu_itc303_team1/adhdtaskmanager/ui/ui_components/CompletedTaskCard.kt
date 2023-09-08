package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun CompletedTaskCard(todo: Todo) {

    // If completion date = "" then make parsedDate Minimum Date
    val parsedDate: LocalDateTime = if (todo.completedDate == ""){
        LocalDateTime.MIN
    } else {
        LocalDateTime.parse(todo.completedDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    }

    val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm a"))

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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = todo.title,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.weight(1f))

            }
            Row(
                modifier = Modifier.height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.description,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Completed On:"
                )

            }
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = when(todo.priority) {
                    1 -> "Low"
                    2 -> "Medium"
                    3 -> "High"
                    else -> "None"
                }
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = when (todo.category) {
                        "Work" -> "Work"
                        "School" -> "School"
                        "Personal" -> "Personal"
                        "Other" -> "Other"
                        else -> "None"
                    }
                )
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueDate)
                Spacer(Modifier.width(4.dp))
                Text(text = todo.dueTime)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = formattedDate)

            }
        }
    }
}