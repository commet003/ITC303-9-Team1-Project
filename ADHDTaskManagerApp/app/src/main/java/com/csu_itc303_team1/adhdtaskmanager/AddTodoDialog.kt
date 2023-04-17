package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {onEvent(TodoEvent.hideDialog)},
        title = {Text(text = "Add Todo")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(TodoEvent.setTitle(it))
                    },
                )
                TextField(
                    value = state.description,
                    onValueChange = {
                        onEvent(TodoEvent.setDescription(it))
                    },
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RadioButton(
                        selected = state.priority == Priority.LOW,
                        onClick = {
                            onEvent(TodoEvent.setPriority(Priority.LOW))
                        })
                    Text(text = Priority.LOW.name)
                    RadioButton(
                        selected = state.priority == Priority.MEDIUM,
                        onClick = {
                            onEvent(TodoEvent.setPriority(Priority.MEDIUM))
                        })
                    Text(text = Priority.MEDIUM.name)
                    RadioButton(
                        selected = state.priority == Priority.HIGH,
                        onClick = {
                            onEvent(TodoEvent.setPriority(Priority.HIGH))
                        })
                    Text(text = Priority.HIGH.name)
                }
                // Date Picker
                // Time Picker

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(TodoEvent.saveTodo)
                }
            ) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(TodoEvent.hideDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}