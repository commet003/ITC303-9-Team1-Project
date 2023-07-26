package com.csu_itc303_team1.adhdtaskmanager.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerScreen() {

    val time = LocalTime.now()

    val timePickerState = remember {
        TimePickerState(
            is24Hour = false,
            initialHour = time.hour,
            initialMinute = time.minute
        )
    }

    TimePicker(state = timePickerState)
}