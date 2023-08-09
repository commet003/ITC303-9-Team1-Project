package com.csu_itc303_team1.adhdtaskmanager.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Priority
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


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
    state.dueDate = (thisTodo?.dueDate ?: LocalDate.now()).toString()
    state.dueTime = (thisTodo?.dueTime ?: LocalTime.now()).toString()


    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onEvent(TodoEvent.hideDialog) },
        title = { Text(text = "Edit Todo") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    TextField(
                        value = thisTodo?.title ?: "",
                        onValueChange = {
                            thisTodo?.title = it
                            onEvent(TodoEvent.setTitle(it))
                        },
                        label = {
                            Text("Enter Title of the task")
                        } // This line adds a hint to the TextField
                    )
                    TextField(
                        value = thisTodo?.description ?: "",
                        onValueChange = {
                            thisTodo?.description = it
                            onEvent(TodoEvent.setDescription(it))
                        },
                        label = {
                            Text("Provide a brief description")
                        }
                    )


                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                )
                {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {

                            RadioButton(
                                selected = thisTodo?.priority == Priority.LOW,
                                onClick = {
                                    thisTodo?.priority = Priority.LOW
                                    onEvent(TodoEvent.setPriority(Priority.LOW))
                                }
                            )


                        Text(text = Priority.LOW.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                            RadioButton(
                                selected = thisTodo?.priority == Priority.MEDIUM,
                                onClick = {
                                    thisTodo?.priority = Priority.MEDIUM
                                    onEvent(TodoEvent.setPriority(Priority.MEDIUM))
                                })



                        Text(text = Priority.MEDIUM.name)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                            RadioButton(
                                selected = thisTodo?.priority == Priority.HIGH,
                                onClick = {
                                    thisTodo?.priority = Priority.HIGH
                                    onEvent(TodoEvent.setPriority(Priority.HIGH))
                                }
                            )
                        Text(text = Priority.HIGH.name)
                    }
                }

                // Date Picker && Time Picker
                val editedPickedDate by remember { // date variable stored to remember
                    mutableStateOf(LocalDateTime.now())
                }

                val dateFormatter: DatePickerFormatter = remember {
                    object : DatePickerFormatter {
                        override fun formatDate(
                            dateMillis: Long?,
                            locale: CalendarLocale,
                            forContentDescription: Boolean
                        ): String? {
                            return dateMillis?.let {
                                val date = LocalDateTime.ofEpochSecond(
                                    it / 1000,
                                    0,
                                    ZoneOffset.UTC
                                )
                                date.toString()
                            }
                        }

                        override fun formatMonthYear(
                            monthMillis: Long?,
                            locale: CalendarLocale
                        ): String? {
                            // Format month and year
                            return monthMillis?.let {
                                val date = LocalDateTime.ofEpochSecond(
                                    it / 1000,
                                    0,
                                    ZoneOffset.UTC
                                )
                                date.toString()
                            }
                        }
                    }
                }

                val selectableDates: SelectableDates? = null

                val editDatePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = editedPickedDate.toEpochSecond(ZoneOffset.UTC) * 1000,
                    yearRange = (LocalDate.now().year..LocalDate.now().year + 3),
                    initialDisplayMode = DisplayMode.Picker,
                    initialDisplayedMonthMillis = null,
                    selectableDates = selectableDates.let {
                        object : SelectableDates {
                            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                                return utcTimeMillis >= LocalDate.now().toEpochDay()
                            }
                        }
                    }
                )

                val editTimePickerState = rememberTimePickerState(
                    initialHour = LocalTime.now().hour,
                    initialMinute = LocalTime.now().minute,
                    is24Hour = false
                )


                val amPMEdited = remember {
                    mutableStateOf("")
                }

                val pmHoursEdited = remember {
                    mutableStateOf(0)
                }


                if (state.showEditDateSelector) {
                    DatePickerDialog(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        onDismissRequest = { onEvent(TodoEvent.hideEditDateSelector) },
                        confirmButton = {
                            Button(onClick = {
                                onEvent(
                                    TodoEvent.setDueDate(
                                        dateFormatter.formatDate(
                                            editDatePickerState.selectedDateMillis,
                                            CalendarLocale.getDefault()
                                        )?.dropLast(6) ?: ""
                                    )
                                )
                                thisTodo?.dueDate = dateFormatter.formatDate(editDatePickerState.selectedDateMillis, CalendarLocale.getDefault())?.dropLast(6) ?: ""
                                onEvent(TodoEvent.hideEditDateSelector)
                            }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),

                                onClick = {
                                    onEvent(TodoEvent.hideEditDateSelector)
                                }) {
                                Text(text = "Cancel")
                            }},
                        shape = MaterialTheme.shapes.large,
                        content = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                DatePicker(
                                    modifier = Modifier.padding(top = 16.dp),
                                    state = editDatePickerState,
                                    showModeToggle = false,
                                    title = null
                                )
                            }
                        },
                    )
                }

                if (state.showEditTimeSelector) {
                    // Time Picker Dialog
                    DatePickerDialog(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        onDismissRequest = { onEvent(TodoEvent.hideEditTimeSelector) },
                        confirmButton = {
                            Button(onClick = {
                               onEvent(
                                        TodoEvent.setDueTime(
                                            ("%02d".format(editTimePickerState.hour)
                                                    + ":" + "%02d".format(editTimePickerState.minute))
                                        )
                                    )

                                    thisTodo?.dueTime = ("%02d".format(editTimePickerState.hour)
                                            + ":" + "%02d".format(editTimePickerState.minute))


                                onEvent(TodoEvent.hideEditTimeSelector)
                            }) {
                                Text(text = "Confirm")
                            }
                        },
                        dismissButton = {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),

                                onClick = {
                                    onEvent(TodoEvent.hideEditTimeSelector)
                                }) {
                                Text(text = "Cancel")
                            }},
                        shape = MaterialTheme.shapes.large,
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 25.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                TimePicker(
                                    colors = TimePickerDefaults.colors(
                                        clockDialColor = MaterialTheme.colorScheme.background,
                                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
                                        selectorColor = MaterialTheme.colorScheme.primary,
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        periodSelectorBorderColor = MaterialTheme.colorScheme.onPrimary,
                                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                        periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground,
                                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onBackground
                                    ),
                                    state = editTimePickerState,
                                    layoutType = TimePickerLayoutType.Vertical,
                                )
                            }
                        },
                    )
                }




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
                            onEvent(TodoEvent.showEditDateSelector)
                        }) {
                            Text(text = "Date")
                        }
                        Text(text = state.dueDate)

                    }
                    // On Button Click it opens the Time Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Button(onClick = {
                            onEvent(TodoEvent.showEditTimeSelector)
                        }) {
                            Text(text = "Time")
                        }
                        if (editTimePickerState.hour > 12) {
                            amPMEdited.value = "PM"
                            pmHoursEdited.value = editTimePickerState.hour - 12
                            Text(text = ("%02d".format(pmHoursEdited.value) + ":"+ "%02d".format(editTimePickerState.minute) + " ${amPMEdited.value}"))
                        } else if (editTimePickerState.hour <= 12) {
                            amPMEdited.value = "AM"
                            Text(text = state.dueTime + " ${amPMEdited.value}")
                        } else {
                            Text(text = "")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(TodoEvent.updateTodo(thisTodo!!))
                    onEvent(TodoEvent.toggleIsClicked(thisTodo!!))
                    onEvent(TodoEvent.resetState)
                }
            ) {
                Text(text = "Edit")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    thisTodo?.let { TodoEvent.toggleIsClicked(it) }?.let { onEvent(it) }
                    onEvent(TodoEvent.resetState)
                    onEvent(TodoEvent.resetTodos)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}