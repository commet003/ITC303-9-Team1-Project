package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo

@Composable
fun TodoCard(todo: Todo, onEvent: (TodoEvent) -> Unit, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(160.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)) {
            Row{
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(25.dp),
                    verticalAlignment = CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(25.dp),
                        verticalAlignment = CenterVertically,
                    ) {
                        Text(
                            text = todo.title,
                            fontSize = 22.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Row{
                    IconButton(
                        onClick = {
                            onEvent(TodoEvent.showEditTodoDialog)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Todo"
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.height(60.dp)) {
                Text(text = todo.description)
            }
        }
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = CenterVertically
        ){
            Text(text = todo.priority.name)
            Text(text = todo.dueDate)
            Text(text = todo.dueTime)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onEvent(TodoEvent.deleteTodo(todo))
                }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Todo"
                )
            }
        }
    }
}