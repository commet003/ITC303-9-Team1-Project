package com.csu_itc303_team1.adhdtaskmanager.screens.tasks


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.liveData
import com.csu_itc303_team1.adhdtaskmanager.EDIT_TASK_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.SETTINGS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID
import com.csu_itc303_team1.adhdtaskmanager.data.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferencesRepository
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConfigurationService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.StorageService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class TasksUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)

@HiltViewModel
class TasksViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService,
    private val userPreferencesRepository: UserPreferencesRepository
) : MainViewModel(logService) {
    private val options = mutableStateOf<List<String>>(listOf())

    val initialSetupEvent = liveData {
        emit(userPreferencesRepository.getAllPreferences())
    }

    private fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        // filter the tasks
        val filteredTasks = if (showCompleted) {
            tasks
        } else {
            tasks.filter { !it.completed }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DEADLINE -> filteredTasks.sortedWith(
                compareByDescending<Task>{ it.dueTime }.thenBy{ it.dueDate }
            )
            SortOrder.BY_PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTasks.sortedWith(
                compareByDescending<Task>
                { it.dueTime }.thenBy { it.dueDate}.thenBy { it.priority }
            )
            SortOrder.BY_CATEGORY -> filteredTasks.sortedBy { it.category }
            SortOrder.BY_DEADLINE_AND_CATEGORY -> filteredTasks.sortedWith(
                compareByDescending<Task>
                { it.dueTime }.thenBy { it.dueDate }.thenBy { it.category }
            )
        }
    }



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
            TaskActionOption.DeleteTask -> onDeleteTaskChange(task)
        }
    }

    private fun onDeleteTaskChange(task: Task) {
        launchCatching { storageService.delete(task.id) }
    }
}