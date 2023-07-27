package com.csu_itc303_team1.adhdtaskmanager


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
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
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.unit.dp
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

                // Date Picker && Time Picker
                var pickedDate by remember {            // date variable stored to remember
                    mutableStateOf(LocalDateTime.now())
                }
                var pickedTime by remember {            // time variable stored to remember
                    mutableStateOf(LocalTime.NOON)
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



                if (state.showDateSelector) {
                    DatePickerDialog(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        onDismissRequest = { onEvent(TodoEvent.hideDateSelector) },
                        confirmButton = {
                            Button(onClick = {
                                dateFormatter.formatDate(datePickerState.selectedDateMillis, CalendarLocale.getDefault())
                                    ?.let { TodoEvent.setDueDate(it.dropLast(6)) }?.let { onEvent(it) }
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
                        }},
                        shape = MaterialTheme.shapes.large,
                        content = {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                DatePicker(
                                    modifier = Modifier.padding(8.dp),
                                    state = datePickerState,
                                    showModeToggle = false,
                                    title = null
                                )
                            }
                        },
                    )
                }

                if (state.showTimeSelector) {
                    // Time Picker Dialog
                    DatePickerDialog(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally),
                        onDismissRequest = { onEvent(TodoEvent.hideTimeSelector) },
                        confirmButton = {
                            Button(onClick = {
                                onEvent(TodoEvent.setDueTime(timePickerState.hour.toString() + ":" + timePickerState.minute.toString()))
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
                            }},
                        shape = MaterialTheme.shapes.large,
                        content = {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                TimePicker(
                                    state = timePickerState,
                                    layoutType = TimePickerLayoutType.Vertical
                                )
                            }
                        },
                    )
                }




                // A New Row containing Two Columns. One for Date, and one for Time
                Row(
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
                        Text(text = state.dueTime)
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