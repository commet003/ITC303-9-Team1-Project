package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.database.local.Todo
import com.csu_itc303_team1.adhdtaskmanager.database.local.TodoDao

@Composable

fun TodoCard(todo: Todo, onEvent: (TodoEvent) -> Unit, onClick: () -> Unit){
    val todoDao: TodoDao
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
                modifier = Modifier.height(60.dp)
            ) {
                Text(text = todo.description)

                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        //todo.isCompleted = true
                        //val updatedTodo = true
                        val updatedTodo = todo.copy(isCompleted = true)
                        // Handle completed task event
                        println("Completed" + todo.isCompleted)
                        //onEvent(TodoEvent.markTodoAsCompleted(updatedTodo))

                        //val completedTodo = todo.markAsCompleted()


                        // onEvent(TodoEvent.markTodoAsCompleted(todo))

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Mark as Completed"
                    )
                }
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
}
