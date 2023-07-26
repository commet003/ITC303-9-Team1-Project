package com.csu_itc303_team1.adhdtaskmanager.components

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import java.time.LocalDate

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerScreen() {

    val date = LocalDate.now()



    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date.toEpochDay(),
        initialDisplayedMonthMillis = null,
        yearRange = date.year..date.year + 1,
        initialDisplayMode = DisplayMode.Picker
    )


    DatePicker(state = datePickerState)
}