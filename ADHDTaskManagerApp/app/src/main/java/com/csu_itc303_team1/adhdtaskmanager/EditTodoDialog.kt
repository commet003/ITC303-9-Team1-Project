package com.csu_itc303_team1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.Priority
import com.csu_itc303_team1.adhdtaskmanager.Todo
import com.csu_itc303_team1.adhdtaskmanager.TodoEvent
import com.csu_itc303_team1.adhdtaskmanager.TodoState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeParseException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var thisTodo: Todo? = null

    for (todo in state.todos) {
        if (todo.isClicked) {
            thisTodo = todo
        }
    }
    state.title = thisTodo?.title ?: ""
    state.description = thisTodo?.description ?: ""
    state.priority = thisTodo?.priority ?: Priority.LOW
    state.dueDate = thisTodo?.dueDate ?: ""
    state.dueTime = thisTodo?.dueTime ?: ""
    state.userId = thisTodo?.userID ?: ""
    state.id = thisTodo?.id ?: 0

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TodoEvent.hideDialog) },
        title = { Text(text = "Edit Todo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (thisTodo != null) {
                    TextField(
                        value = thisTodo.title,
                        onValueChange = {
                            thisTodo.title = it
                            onEvent(TodoEvent.setTitle(it))
                        },
                        label = {
                            Text(
                                "Enter Title of the task",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } // This line adds a hint to the TextField
                    )
                }

                if (thisTodo != null) {
                    TextField(
                        value = thisTodo.description,
                        onValueChange = {
                            thisTodo.description = it
                            onEvent(TodoEvent.setDescription(it))
                        },
                        label = {
                            Text(
                                "Provide a brief description",
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } // This line adds a hint to the TextField

                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                        if (thisTodo != null) {
                            RadioButton(
                                selected = thisTodo.priority == Priority.LOW,
                                onClick = {
                                    if (thisTodo != null) {
                                        thisTodo.priority = Priority.LOW
                                    }
                                    onEvent(TodoEvent.setPriority(Priority.LOW))
                                })
                        }

                        Text(text = Priority.LOW.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (thisTodo != null) {
                            RadioButton(
                                selected = thisTodo.priority == Priority.MEDIUM,
                                onClick = {
                                    if (thisTodo != null) {
                                        thisTodo.priority = Priority.MEDIUM
                                    }
                                    onEvent(TodoEvent.setPriority(Priority.MEDIUM))
                                })
                        }

                        Text(text = Priority.MEDIUM.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (thisTodo != null) {
                            RadioButton(
                                selected = thisTodo.priority == Priority.HIGH,
                                onClick = {
                                    if (thisTodo != null) {
                                        thisTodo.priority = Priority.HIGH
                                    }
                                    onEvent(TodoEvent.setPriority(Priority.HIGH))
                                })
                        }

                        Text(text = Priority.HIGH.name)
                    }
                }

                var displayDate by remember {
                    mutableStateOf(state.dueDate)
                }

                var displayTime by remember {
                    mutableStateOf(state.dueTime)
                }


                // Time and Date Converted from String to LocalTime and LocalDate
                var convertedDate by remember {
                    mutableStateOf(
                        try {
                            LocalDate.parse(thisTodo?.dueDate)
                        } catch (e: DateTimeParseException) {
                            LocalDate.now()
                        }
                    )
                }

                var convertedTime by remember {
                    mutableStateOf(
                        try {
                            LocalTime.parse(thisTodo?.dueTime)
                        } catch (e: DateTimeParseException) {
                            LocalTime.NOON
                        }
                    )
                }


                // Date Picker && Time Picker
                var editedPickedDate by remember { // date variable stored to remember
                    mutableStateOf(LocalDate.now())
                }

                var editedPickedTime by remember { // time variable stored to remember
                    mutableStateOf(LocalTime.NOON)
                }


                // Date and Time Dialog States
                val dateDialogState = rememberDatePickerState()
                val timeDialogState = rememberTimePickerState()


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
                        Button(onClick = {

                        }) {
                            Text(text = "Date")
                        }
                        if (thisTodo != null) {
                            Text(text = displayDate)
                        }
                    }
                    // On Button Click it opens the Time Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Button(onClick = {
                        }) {
                            Text(text = "Time")
                        }
                        if (thisTodo != null) {
                            Text(text = displayTime)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (thisTodo != null) {
                        thisTodo.dueDate = state.dueDate
                        thisTodo.dueTime = state.dueTime
                    }
                    onEvent(TodoEvent.toggleIsClicked(thisTodo!!))
                    onEvent(TodoEvent.updateTodo)
                }
            ) {
                Text(text = "Edit")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(TodoEvent.toggleIsClicked(thisTodo!!))
                    onEvent(TodoEvent.hideEditTodoDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}