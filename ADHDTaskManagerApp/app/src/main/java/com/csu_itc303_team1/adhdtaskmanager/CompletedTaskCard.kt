package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.EditTodoDialog
import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CompletedTaskCard(todo: Todo) {

    val parsedDate = LocalDateTime.parse(todo.completionDate, DateTimeFormatter.ISO_DATE_TIME)
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
                Text(text = todo.priority.name)
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