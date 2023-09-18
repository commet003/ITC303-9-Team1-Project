package com.csu_itc303_team1.adhdtaskmanager.screens.edit_task


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicField
import com.csu_itc303_team1.adhdtaskmanager.common.composable.CategoryDropdown
import com.csu_itc303_team1.adhdtaskmanager.common.composable.PriorityDropdown
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RegularCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.ext.card
import com.csu_itc303_team1.adhdtaskmanager.common.ext.fieldModifier
import com.csu_itc303_team1.adhdtaskmanager.common.ext.spacer
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.csu_itc303_team1.adhdtaskmanager.R.drawable as AppIcon
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.S)
@Composable
@ExperimentalMaterialApi
fun EditTaskScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState(emptyList())
    for (task in tasks){
        if(task.edit){
            viewModel.task = remember {
                mutableStateOf(task)
            }
        }
    }
    var task = viewModel.task.value

    Log.d("EditTaskScreen", "$task")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (task.edit){
                        viewModel.task.value = viewModel.task.value.copy(edit = false)
                    }
                    viewModel.onDoneClick(popUpScreen)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Done, "Done")
            }
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.spacer())

            val fieldModifier = Modifier.fieldModifier()
            BasicField(AppText.title, task.title, viewModel::onTitleChange, fieldModifier)
            BasicField(AppText.description, task.description, viewModel::onDescriptionChange, fieldModifier)

            Spacer(modifier = Modifier.spacer())
            CardEditors(task, viewModel::onDateChange, viewModel::onTimeChange)
            PriorityDropdown(task = task, onNewValue = viewModel::onPriorityChange)
            CategoryDropdown(task = task, onNewValue = viewModel::onCategoryChange)
            Spacer(modifier = Modifier.spacer())
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CardEditors(
    task: Task,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit
) {
    val activity = LocalContext.current as AppCompatActivity

    RegularCardEditor(AppText.date, AppIcon.ic_calendar, task.dueDate, Modifier.card()) {
        showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, task.dueTime, Modifier.card()) {
        showTimePicker(activity, onTimeChange)
    }
}

private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
    }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
    val picker = MaterialTimePicker.Builder()
        .setTimeFormat(TimeFormat.CLOCK_12H)
        .build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
    }
}