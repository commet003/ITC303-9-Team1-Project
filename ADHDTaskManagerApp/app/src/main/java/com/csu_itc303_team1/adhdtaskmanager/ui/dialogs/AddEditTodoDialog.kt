package com.csu_itc303_team1.adhdtaskmanager.ui.dialogs


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager.AlarmItem
import com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager.AlarmSchedulerImpl
import com.csu_itc303_team1.adhdtaskmanager.utils.states.TodoState
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Priority
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.TodoEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTodoDialog(
    state: TodoState,
    onEvent: (TodoEvent) -> Unit,
    scope: CoroutineScope,
    sheetState: SheetState,
    alarmScheduler: AlarmSchedulerImpl,
    modifier: Modifier = Modifier
) {
    var thisTodo: Todo? = null
    val reminderSet = remember { mutableStateOf(false) }
    var alarmItem: AlarmItem? = null

    if (state.showEditTodoDialog) {
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
        state.reminderSet = thisTodo?.reminderSet ?: false
    }


    var priorityExpandedMenu by remember { mutableStateOf(false) }
    var prioritySelection by remember { mutableStateOf("") }
    if (state.showEditTodoDialog) {
        prioritySelection = thisTodo?.priority?.name ?: ""
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text =
            if (state.showDialog){
                "Add a new task"
            } else{
                "Edit task"
            },
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
        }



        Column(
            modifier = modifier.height(200.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            TextField(
                modifier = Modifier
                    .border(
                        BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.medium
                    )
                    .width(280.dp),
                singleLine = true,
                value = if (state.showDialog){
                    state.title
                }else {
                    thisTodo?.title ?: ""
                }
                ,
                onValueChange = {
                    if (state.showDialog){
                        onEvent(TodoEvent.setTitle(it))
                    } else if (state.showEditTodoDialog){
                        thisTodo?.title = it
                        onEvent(TodoEvent.setTitle(it))
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = MaterialTheme.shapes.medium,
                isError = state.titleError,
                label = { Text("Enter Title of the task") } // This line adds a hint to the TextField
            )
            TextField(
                modifier = Modifier
                    .border(
                        BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.medium
                    )
                    .width(280.dp),
                singleLine = false,
                maxLines = 4,
                value = if (state.showDialog) {
                    state.description
                } else {
                    thisTodo?.description ?: ""
                },
                onValueChange = {
                    if (state.showDialog){
                        onEvent(TodoEvent.setDescription(it))
                    } else if (state.showEditTodoDialog){
                        thisTodo?.description = it
                        onEvent(TodoEvent.setDescription(it))
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = MaterialTheme.shapes.medium,
                isError = state.descriptionError,
                label = { Text("Provide a brief description") } // This line adds a hint to the TextField
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ){
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
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
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
                                if (state.showDialog) {
                                    priorityExpandedMenu = false
                                    prioritySelection = priorityLevel.name
                                    onEvent(TodoEvent.setPriority(priorityLevel))
                                } else if (state.showEditTodoDialog) {
                                    priorityExpandedMenu = false
                                    thisTodo?.priority = priorityLevel
                                    onEvent(TodoEvent.setPriority(priorityLevel))
                                }

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
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Set Reminder", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(10.dp))
                FilledIconToggleButton(
                    checked = reminderSet.value,
                    onCheckedChange = {
                        if (state.showDialog) {
                            reminderSet.value = !reminderSet.value
                            state.reminderSet = reminderSet.value
                            Log.d("Reminder", state.reminderSet.toString())
                        } else if (state.showEditTodoDialog) {
                            reminderSet.value = !reminderSet.value
                            onEvent(TodoEvent.toggleReminder(thisTodo!!))
                        }
                    },
                    colors = IconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onSurface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        checkedContainerColor = MaterialTheme.colorScheme.secondary,
                        checkedContentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Alarm,
                        contentDescription = "Set Reminder"
                    )
                }
            }
        }



        // Date Picker && Time Picker
        val pickedDate by remember {            // date variable stored to remember
            mutableStateOf(LocalDateTime.now())
        }

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

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = pickedDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            yearRange = (LocalDate.now().year..LocalDate.now().year + 3),
            initialDisplayMode = DisplayMode.Picker,
            initialDisplayedMonthMillis = null
        )

        val editDatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = editedPickedDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            yearRange = (LocalDate.now().year..LocalDate.now().year + 3),
            initialDisplayMode = DisplayMode.Picker,
            initialDisplayedMonthMillis = null
        )

        val timePickerState = rememberTimePickerState(
            initialHour = LocalTime.now().hour,
            initialMinute = LocalTime.now().minute,
            is24Hour = false
        )

        val editTimePickerState = rememberTimePickerState(
            initialHour = LocalTime.now().hour,
            initialMinute = LocalTime.now().minute,
            is24Hour = false
        )

        val amPM = remember {
            mutableStateOf("")
        }

        val pmHours = remember {
            mutableIntStateOf(0)
        }

        val amPMEdited = remember {
            mutableStateOf("")
        }

        val pmHoursEdited = remember {
            mutableIntStateOf(0)
        }



        if (state.showDateSelector || state.showEditDateSelector) {
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
                                if (state.showDialog){
                                    dateFormatter.formatDate(
                                        datePickerState.selectedDateMillis,
                                        CalendarLocale.getDefault()
                                    )
                                        ?.let { TodoEvent.setDueDate(it.dropLast(6)) }
                                        ?.let { onEvent(it) }
                                    onEvent(TodoEvent.hideDateSelector)
                                }else if (state.showEditTodoDialog){
                                    TodoEvent.setDueDate(
                                        dateFormatter.formatDate(
                                            editDatePickerState.selectedDateMillis,
                                            CalendarLocale.getDefault()
                                        )?.dropLast(6) ?: ""
                                    )
                                    thisTodo?.dueDate = dateFormatter.formatDate(
                                        editDatePickerState.selectedDateMillis,
                                        CalendarLocale.getDefault()
                                    )?.dropLast(6) ?: ""
                                    onEvent(TodoEvent.hideEditDateSelector)
                                }

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
                                if (state.showDialog) {
                                    onEvent(TodoEvent.hideDateSelector)
                                } else if (state.showEditTodoDialog){
                                    onEvent(TodoEvent.hideEditDateSelector)
                                }
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
                                state = if (state.showDialog){
                                    datePickerState
                                } else if (state.showEditTodoDialog){
                                    editDatePickerState
                                } else {
                                    datePickerState
                                },
                                showModeToggle = false,
                                title = null
                            )
                        }
                    },
                )
            }
        }

        if (state.showTimeSelector || state.showEditTimeSelector) {
            Column {
                // Time Picker Dialog
                DatePickerDialog(
                    modifier = Modifier
                        .fillMaxSize(),
                    onDismissRequest = {
                        if(state.showDialog) {
                            onEvent(TodoEvent.hideTimeSelector)
                        } else {
                            onEvent(TodoEvent.hideEditTimeSelector)
                        }
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {
                                if (state.showDialog) {
                                    onEvent(
                                        TodoEvent.setDueTime(
                                            ("%02d".format(timePickerState.hour)
                                                    + ":" + "%02d".format(timePickerState.minute))
                                        )
                                    )
                                    onEvent(TodoEvent.hideTimeSelector)
                                } else{
                                    onEvent(
                                        TodoEvent.setDueTime(
                                            ("%02d".format(editTimePickerState.hour)
                                                    + ":" + "%02d".format(editTimePickerState.minute))
                                        )
                                    )

                                    thisTodo?.dueTime = ("%02d".format(editTimePickerState.hour)
                                            + ":" + "%02d".format(editTimePickerState.minute))

                                    onEvent(TodoEvent.hideEditTimeSelector)
                                }

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
                                if (state.showDialog) {
                                    onEvent(TodoEvent.hideTimeSelector)
                                } else{
                                    onEvent(TodoEvent.hideEditTimeSelector)
                                }
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
                                state =
                                if (state.showDialog) {
                                    timePickerState
                                } else{
                                    editTimePickerState
                                },
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
                        if (state.showDialog) {
                            onEvent(TodoEvent.showDateSelector)
                        } else {
                            onEvent(TodoEvent.showEditDateSelector)
                        }
                    }
                )
                {
                    Text(text = "Date", fontWeight = FontWeight.Bold)
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
                        if (state.showDialog) {
                            onEvent(TodoEvent.showTimeSelector)
                        } else {
                            onEvent(TodoEvent.showEditTimeSelector)
                        }
                    }) {
                    Text(text = "Time", fontWeight = FontWeight.Bold)
                }
                if (state.showDialog){
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
                } else {
                    if (editTimePickerState.hour > 12) {
                        amPM.value = "PM"
                        pmHours.value = editTimePickerState.hour - 12
                        Text(
                            text = ("%02d".format(pmHours.value) + ":" + "%02d".format(
                                editTimePickerState.minute
                            ) + " ${amPM.value}")
                        )
                    } else if (editTimePickerState.hour <= 12) {
                        amPM.value = "AM"
                        Text(text = state.dueTime + " ${amPM.value}")
                    } else {
                        Text(text = "")
                    }
                }

            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                onClick = {
                    if (state.showDialog){
                        alarmItem = AlarmItem(
                            id = state.id,
                            time = LocalDateTime.of(
                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!).atZone(ZoneId.systemDefault()).toLocalDate(),
                                LocalTime.of(timePickerState.hour, timePickerState.minute)
                            ),
                            title = state.title,
                            description = state.description,
                            isOn = state.reminderSet
                        )
                        Log.d("AlarmItem", alarmItem!!.time.toString() ?: "No Alarm")
                        alarmItem?.let { alarmScheduler::scheduleAlarm }
                        onEvent(TodoEvent.resetState)
                        scope.launch {
                            sheetState.hide()
                        }
                    } else if (state.showEditTodoDialog){
                        alarmItem = AlarmItem(
                            id = thisTodo!!.id,
                            time = LocalDateTime.of(
                                LocalDate.ofEpochDay(editDatePickerState.selectedDateMillis!!),
                                LocalTime.of(editTimePickerState.hour, editTimePickerState.minute)
                            ),
                            title = thisTodo.title,
                            description = thisTodo.description,
                            isOn = thisTodo.reminderSet
                        )
                        alarmItem?.let { alarmScheduler::scheduleAlarm }
                        thisTodo.let { TodoEvent.toggleIsClicked(it) }.let { onEvent(it) }
                        onEvent(TodoEvent.resetState)
                        onEvent(TodoEvent.resetTodos)
                        scope.launch {
                            sheetState.hide()
                        }
                    }
                }
            ) {
                Text(text = "Cancel", fontWeight = FontWeight.Bold)
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {
                    if (state.title.isNotEmpty() && state.description.isNotEmpty()) {
                        if (state.showDialog) {
                            onEvent(TodoEvent.saveTodo)
                            scope.launch {
                                sheetState.hide()
                            }
                        } else if (state.showEditTodoDialog) {
                            onEvent(TodoEvent.updateTodo(thisTodo!!))
                            onEvent(TodoEvent.toggleIsClicked(thisTodo))
                            onEvent(TodoEvent.resetState)
                            scope.launch {
                                sheetState.hide()
                            }
                        }
                    }
                    if (state.title.isEmpty()){
                        onEvent(TodoEvent.titleError(true))
                    }else{
                        onEvent(TodoEvent.titleError(false))
                    }
                    if (state.description.isEmpty()){
                        onEvent(TodoEvent.descriptionError(true))
                    }else {
                        onEvent(TodoEvent.descriptionError(false))
                    }

                }
            ) {
                if (state.showDialog){
                    Text(text = "Add Task", fontWeight = FontWeight.Bold)
                } else if (state.showEditTodoDialog){
                    Text(text = "Edit Task", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}