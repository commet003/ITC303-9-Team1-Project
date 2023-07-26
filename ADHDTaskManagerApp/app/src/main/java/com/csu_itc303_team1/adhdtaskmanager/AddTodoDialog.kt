package com.csu_itc303_team1.adhdtaskmanager


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.components.DatePickerScreen
import com.csu_itc303_team1.adhdtaskmanager.components.TimePickerScreen


@Composable
fun AddTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {onEvent(TodoEvent.hideDialog)},
        title = {Text(text = "Add Todo", modifier = Modifier.padding(bottom = 10.dp))},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    modifier = Modifier.focusTarget(),
                    value = state.title,
                    onValueChange = {
                        onEvent(TodoEvent.setTitle(it))
                    },
                    label = { Text("Enter Title of the task") } // This line adds a hint to the TextField
                )
                TextField(
                    value = state.description,
                    onValueChange = {
                        onEvent(TodoEvent.setDescription(it))
                    },
                    label = { Text("Provide a brief description") } // This line adds a hint to the TextField

                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp))
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                        ) {
                        RadioButton(
                            selected = state.priority == Priority.LOW,
                            onClick = {
                                onEvent(TodoEvent.setPriority(Priority.LOW))
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = Priority.LOW.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        RadioButton(
                            selected = state.priority == Priority.MEDIUM,
                            onClick = {
                                onEvent(TodoEvent.setPriority(Priority.MEDIUM))
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = Priority.MEDIUM.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        RadioButton(
                            selected = state.priority == Priority.HIGH,
                            onClick = {
                                onEvent(TodoEvent.setPriority(Priority.HIGH))
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(text = Priority.HIGH.name)
                    }
                }

                val datePickerScreen = DatePickerScreen()
                val timePickerScreen = TimePickerScreen()

                // A New Row containing Two Columns. One for Date, and one for Time
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,


                ) {
                    // On Button Click it opens the date Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                datePickerScreen
                        }) {
                            Text(
                                color = MaterialTheme.colorScheme.onPrimary,
                                text = "Date")
                        }

                    }
                    // On Button Click it opens the Time Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                            timePickerScreen
                        }) {
                            Text(text = "Time")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onEvent(TodoEvent.saveTodo)
                }
            ) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onEvent(TodoEvent.hideDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}