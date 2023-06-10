package com.csu_itc303_team1.adhdtaskmanager

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.border

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.csu_itc303_team1.adhdtaskmanager.db.DBHelper

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
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
                            })
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
                            })
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
                            })
                        Text(text = Priority.HIGH.name)
                    }
                }
                // Date Picker && Time Picker
                var pickedDate by remember {            // date variable stored to remember
                    mutableStateOf(LocalDate.now())
                }
                var pickedTime by remember {            // time variable stored to remember
                    mutableStateOf(LocalTime.NOON)
                }
                val formattedDate by remember {         // date variable formatted to string
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("dd MMM yyyy")   // Format Selected
                            .format(pickedDate)
                    }
                }
                val formattedTime by remember {         // Time variable formatted to string
                    derivedStateOf {
                        DateTimeFormatter
                            .ofPattern("hh:mm a")   // The Format Selected
                            .format(pickedTime)
                    }
                }
                // Date and Time Dialog States
                val dateDialogState = rememberMaterialDialogState()
                val timeDialogState = rememberMaterialDialogState()

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
                            dateDialogState.show()
                        }) {
                            Text(text = "Date")
                        }
                        Text(text = formattedDate)
                    }
                    // On Button Click it opens the Time Dialog Screen,
                    // the text displays default or whatever is chosen
                    Column(
                        modifier = Modifier.padding(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top

                    ) {
                        Button(onClick = {
                            timeDialogState.show()
                        }) {
                            Text(text = "Time")
                        }
                        Text(text = formattedTime)
                    }
                }
                // PomodoroTimer
                val pomodoroTimerDialogState = rememberMaterialDialogState()

                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Button(onClick = {
                        pomodoroTimerDialogState.show()
                    }) {
                        Text(text = "PomodoroTimer")
                    }
                }

                // Inside the Date Dialog Screen, Ok and Cancel Buttons
                MaterialDialog(
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok") {
                        }
                        negativeButton(text = "Cancel")
                    }
                ) {
                    datepicker(
                        // Date Settings, initial date, title and a constraint
                        initialDate = LocalDate.now(),
                        title = "Pick a date",
                        allowedDateValidator = {
                            it.isAfter(LocalDate.now())
                        }
                    ) {
                        // What Happens when Date is picked.
                        // Date Chosen becomes the date variable
                        // the formatted date is then added to the TodoEvent
                        pickedDate = it
                        onEvent(TodoEvent.setDueDate(formattedDate))
                    }

                }
                MaterialDialog(
                    // Inside the Time Dialog Screen, Ok and Cancel Buttons
                    dialogState = timeDialogState,
                    buttons = {
                        positiveButton(text = "Ok") {

                        }
                        negativeButton(text = "Cancel")
                    }
                ) {
                    timepicker(
                        // Time Settings: initial time and title
                        initialTime = LocalTime.NOON,
                        title = "Pick a Time"
                    ) {
                        // What Happens when Time is picked.
                        // Date Chosen becomes the Time variable
                        // the formatted Time is then added to the TodoEvent
                        pickedTime = it
                        onEvent(TodoEvent.setDueTime(formattedTime))
                    }

                }

                MaterialDialog(
                    // Inside the Time Dialog Screen, Ok and Cancel Buttons
                    dialogState = pomodoroTimerDialogState,

                    buttons = {
                        positiveButton(text = "Ok") {
                        }
                        negativeButton(text = "Cancel")
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(30.dp)
                            .background(color = Color(0x6750a4)),
                    ) {
                        val messagePomo = remember{mutableStateOf("00:00")}
                        val messageBreak = remember{mutableStateOf("00:00")}
                        var buttonClicked by remember { mutableStateOf(false) }

                        PomodoroTabs(messagePomo, messageBreak)
                        Button(
                            onClick = {
                                buttonClicked = true
                            },
                            modifier = Modifier.width(250.dp)
                        ) {
                            Text(text = "Set")
                        }
                        if (buttonClicked) {
                            onEvent(TodoEvent.setPomodoroTime(messagePomo.value))
                            onEvent(TodoEvent.setBreakTime(messageBreak.value))
                        }
                    }
                }

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

@Composable
fun PomodoroTabs(pomodoroTime: MutableState<String>, breakTime: MutableState<String>) {
    val tabs = listOf("Pomodoro", "Break")
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(2.dp)
                        .fillMaxWidth(),
                ) {
                    RoundedCornerShape(5.dp)
                }
            },
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        selectedTabIndex = index
                    },
                    text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = if (selectedTabIndex == index) Color(103, 80, 164) else Color.White,
                                background = if (selectedTabIndex == index) Color.White else Color(103, 80, 164)
                            ),
                        )
                    },
                    modifier = Modifier
                        .background(
                            color = if (selectedTabIndex == index) Color.White else Color(
                                103,
                                80,
                                164
                            )
                        )
                        .border(2.dp, Color(103, 80, 164), RoundedCornerShape(20.dp))
                )
            }
        }
        when (selectedTabIndex) {
            0 -> TabContent(pomodoroTime)
            1 -> TabContent(breakTime)
        }
    }
}


@Composable
fun TabContent(message: MutableState<String>) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = message.value,
            onValueChange = {
                newText -> message.value = newText
            },
            textStyle = TextStyle(
                fontSize = 80.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)

        )
    }
}

