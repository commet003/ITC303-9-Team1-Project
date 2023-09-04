package com.csu_itc303_team1.adhdtaskmanager.screens.tasks

import androidx.compose.runtime.mutableStateOf
import com.csu_itc303_team1.adhdtaskmanager.EDIT_TASK_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.SETTINGS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConfigurationService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.StorageService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : MainViewModel(logService) {
    val options = mutableStateOf<List<String>>(listOf())

    val tasks = storageService.tasks

    fun loadTaskOptions() {
        val hasEditOption = configurationService.isShowTaskEditButtonConfig
        options.value = TaskActionOption.getOptions(hasEditOption)
    }

    private fun onTaskCompletedChange(task: Task) {
        launchCatching { storageService.update(task.copy(completed = !task.completed)) }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_TASK_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onTaskActionClick(openScreen: (String) -> Unit, task: Task, action: String) {
        when (TaskActionOption.getByTitle(action)) {
            TaskActionOption.CompleteTask -> onTaskCompletedChange(task)
            TaskActionOption.EditTask -> openScreen("$EDIT_TASK_SCREEN?$TASK_ID={${task.id}}")
            TaskActionOption.DeleteTask -> onDeleteTaskClick(task)
        }
    }

    private fun onDeleteTaskClick(task: Task) {
        launchCatching { storageService.delete(task.id) }
    }
}