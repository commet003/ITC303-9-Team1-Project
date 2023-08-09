package com.csu_itc303_team1.adhdtaskmanager.ui.dialogs


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Priority
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var priorityExpandedMenu by remember { mutableStateOf(false) }
    var prioritySelection by remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier.height(600.dp),
        onDismissRequest = {onEvent(TodoEvent.hideDialog)},
        title = {Text(text = "Add Todo", modifier = Modifier.padding(bottom = 10.dp))},
        text = {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {

                Column(
                    modifier = modifier.height(200.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
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
                }

                ExposedDropdownMenuBox(
                    expanded = priorityExpandedMenu,
                    onExpandedChange = { priorityExpandedMenu = !priorityExpandedMenu }
                )
                {
                    Button(
                        modifier = Modifier
                            .width(130.dp)
                            .menuAnchor(),
                        onClick = { }) {
                        Text(
                            text = prioritySelection.ifEmpty {
                                "Priority"
                            },
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Priority"
                        )
                    }

                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary),
                        expanded = priorityExpandedMenu,
                        onDismissRequest = { priorityExpandedMenu = false }) {
                        Priority.values().forEach { priorityLevel ->
                            DropdownMenuItem(
                                modifier = Modifier.clip(MaterialTheme.shapes.medium),
                                onClick = {
                                    priorityExpandedMenu = false
                                    prioritySelection = priorityLevel.name
                                    onEvent(TodoEvent.setPriority(priorityLevel))
                                },
                                text = {
                                    Text(
                                        text = priorityLevel.name,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            )
                        }
                    }
                }



                // Date Picker && Time Picker
                var pickedDate by remember {            // date variable stored to remember
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

                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = pickedDate.toEpochSecond(ZoneOffset.UTC) * 1000,
                    yearRange = (LocalDate.now().year..LocalDate.now().year + 3),
                    initialDisplayMode = DisplayMode.Picker,
                    initialDisplayedMonthMillis = null
                )

                val timePickerState = rememberTimePickerState(
                    initialHour = LocalTime.now().hour,
                    initialMinute = LocalTime.now().minute,
                    is24Hour = false
                )

                val amPM = remember {
                    mutableStateOf("")
                }

                val pmHours = remember {
                    mutableStateOf(0)
                }



                if (state.showDateSelector) {
                    Column {
                        DatePickerDialog(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.CenterHorizontally),
                            onDismissRequest = { onEvent(TodoEvent.hideDateSelector) },
                            confirmButton = {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    onClick = {
                                        dateFormatter.formatDate(
                                            datePickerState.selectedDateMillis,
                                            CalendarLocale.getDefault()
                                        )
                                            ?.let { TodoEvent.setDueDate(it.dropLast(6)) }
                                            ?.let { onEvent(it) }
                                        onEvent(TodoEvent.hideDateSelector)
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
                                        onEvent(TodoEvent.hideDateSelector)
                                    }) {
                                    Text(text = "Cancel")
                                }
                            },
                            shape = MaterialTheme.shapes.large,
                            content = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                ) {
                                    DatePicker(
                                        state = datePickerState,
                                        showModeToggle = false,
                                        title = null
                                    )
                                }
                            },
                        )
                    }
                }

                if (state.showTimeSelector) {
                    Column {
                        // Time Picker Dialog
                        DatePickerDialog(
                            modifier = Modifier
                                .fillMaxSize(),
                            onDismissRequest = { onEvent(TodoEvent.hideTimeSelector) },
                            confirmButton = {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    onClick = {
                                        onEvent(
                                            TodoEvent.setDueTime(
                                                ("%02d".format(timePickerState.hour)
                                                        + ":" + "%02d".format(timePickerState.minute))
                                            )
                                        )


                                        onEvent(TodoEvent.hideTimeSelector)
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
                                        onEvent(TodoEvent.hideTimeSelector)
                                    }) {
                                    Text(text = "Cancel")
                                }
                            },
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
                                        state = timePickerState,
                                        layoutType = TimePickerLayoutType.Vertical
                                    )
                                }
                            },
                        )
                    }

                }


                // A New Row containing Two Columns. One for Date, and one for Time
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // On Button Click it opens the date Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                onEvent(TodoEvent.showDateSelector)
                            }
                        )
                        {
                            Text(text = "Date")
                        }
                        Text(text = state.dueDate)
                    }
                    // On Button Click it opens the Time Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                onEvent(TodoEvent.showTimeSelector)
                            }) {
                            Text(text = "Time")
                        }
                        if (timePickerState.hour > 12) {
                            amPM.value = "PM"
                            pmHours.value = timePickerState.hour - 12
                            Text(
                                text = ("%02d".format(pmHours.value) + ":" + "%02d".format(
                                    timePickerState.minute
                                ) + " ${amPM.value}")
                            )
                        } else if (timePickerState.hour <= 12) {
                            amPM.value = "AM"
                            Text(text = state.dueTime + " ${amPM.value}")
                        } else {
                            Text(text = "")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(end = 8.dp),
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
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                onClick = {
                    onEvent(TodoEvent.hideDialog)
                    onEvent(TodoEvent.resetState)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}


