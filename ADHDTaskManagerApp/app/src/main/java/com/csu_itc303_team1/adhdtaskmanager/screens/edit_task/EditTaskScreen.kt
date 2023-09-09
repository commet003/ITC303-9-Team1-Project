package com.csu_itc303_team1.adhdtaskmanager.screens.edit_task


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.ActionToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicField
import com.csu_itc303_team1.adhdtaskmanager.common.composable.PriorityDropdown
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RegularCardEditor
import com.csu_itc303_team1.adhdtaskmanager.common.ext.card
import com.csu_itc303_team1.adhdtaskmanager.common.ext.fieldModifier
import com.csu_itc303_team1.adhdtaskmanager.common.ext.spacer
import com.csu_itc303_team1.adhdtaskmanager.common.ext.toolbarActions
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.csu_itc303_team1.adhdtaskmanager.R.drawable as AppIcon
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterialApi
fun EditTaskScreen(
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    val task by viewModel.task

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = if(task.id.isNotEmpty()) AppText.edit_task else AppText.add_task,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_check,
            endAction = { viewModel.onDoneClick(popUpScreen) }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, task.title, viewModel::onTitleChange, fieldModifier)
        BasicField(AppText.description, task.description, viewModel::onDescriptionChange, fieldModifier)

        Spacer(modifier = Modifier.spacer())
        CardEditors(task, viewModel::onDateChange, viewModel::onTimeChange)
        //CardSelectors(task, viewModel::onPriorityChange, viewModel::onCategoryChange)
        PriorityDropdown(task, viewModel::onPriorityChange)
        Spacer(modifier = Modifier.spacer())
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

/*@Composable
@ExperimentalMaterialApi
private fun CardSelectors(
    task: Task,
    onPriorityChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit
) {
    val prioritySelection = Priority.getByName(Priority.getByValue(task.priority).name).name
    CardSelector(AppText.priority, Priority.getOptions(), prioritySelection, Modifier.card()) {
            newValue ->
        onPriorityChange(newValue)
    }

    val categorySelection = Category.getCategoryByName(task.category).name
    CardSelector(AppText.category, Category.getCategories(), categorySelection, Modifier.card()) {
            newValue ->
        onCategoryChange(newValue)
    }
}*/

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