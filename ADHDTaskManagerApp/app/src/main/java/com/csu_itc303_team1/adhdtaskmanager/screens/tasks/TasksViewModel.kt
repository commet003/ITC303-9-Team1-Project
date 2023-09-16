package com.csu_itc303_team1.adhdtaskmanager.screens.tasks


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.EDIT_TASK_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskRepository
import com.csu_itc303_team1.adhdtaskmanager.data.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferences
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferencesRepository
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    logService: LogService,

    private val accountService: AccountService,
    //private val storageService: StorageService,
    private val usersStorageService: UsersStorageService,
    private val localTaskRepository: LocalTaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository,

) : MainViewModel(logService) {

    private var userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    val localTask = localTaskRepository.getAllTasks()

    @RequiresApi(Build.VERSION_CODES.N)
    fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        showUncompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        // filter the tasks
        val filteredTodos = if (showCompleted && showUncompleted) {
            tasks.filter {
                it.userId == accountService.currentUserId
            }
        } else if(!showUncompleted &&  showCompleted) {
            tasks.filter {
                it.completed
                it.userId == accountService.currentUserId
            }
        } else {
            tasks.filter {
                !it.completed
                it.userId == accountService.currentUserId
            }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTodos
            SortOrder.BY_DEADLINE -> filteredTodos.sortedWith(
                compareBy<Task>{ it.dueDate.slice(5..6).toIntOrNull() }
                    .thenBy { it.dueDate.slice(8..9).toIntOrNull() }
                    .thenBy { it.dueDate.slice(11..14).toIntOrNull() }
                    .thenBy{ it.dueTime.slice(0..1).toIntOrNull()}
                    .thenBy { it.dueTime.slice(3..4).toIntOrNull() }
            )
            SortOrder.BY_PRIORITY -> filteredTodos.sortedBy { it.priority }.asReversed()
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTodos.sortedWith(
                compareBy<Task>{ it.priority }.reversed()
                    .thenBy { it.dueDate.slice(5..6).toIntOrNull() }
                    .thenBy { it.dueDate.slice(8..9).toIntOrNull() }
                    .thenBy { it.dueDate.slice(11..14).toIntOrNull() }
                    .thenBy { it.dueTime.slice(0..1).toIntOrNull()}
                    .thenBy { it.dueTime.slice(3..4).toIntOrNull() }

            )
            SortOrder.BY_CATEGORY -> filteredTodos.sortedBy { it.category }
            SortOrder.BY_DEADLINE_AND_CATEGORY -> filteredTodos.sortedWith(
                compareBy<Task>{ it.category }
                    .thenBy { it.dueDate.slice(5..6).toIntOrNull() }
                    .thenBy { it.dueDate.slice(8..9).toIntOrNull() }
                    .thenBy { it.dueDate.slice(11..14).toIntOrNull() }
                    .thenBy{ it.dueTime.slice(0..1).toIntOrNull()}
                    .thenBy { it.dueTime.slice(3..4).toIntOrNull() }
            )
        }
    }



    //val tasks = storageService.tasks

    fun getPreferences(): Flow<UserPreferences> {
        return userPreferencesFlow
    }

    fun setSortOrder(sortOrder: SortOrder) {
        launchCatching {
            userPreferencesRepository.setSortOrder(sortOrder)
        }
    }



    private fun onTaskCompletedChange(task: Task) {
        launchCatching {
           // storageService.update(task.copy(completed = !task.completed))
            localTaskRepository.updateTask(task.copy(completed = !task.completed))
            usersStorageService.incrementUserRewardPoints(task.userId, COMPLETED_TASK_REWARD, COMPLETED_TASK_REWARD_NAME)
        }
    }

    fun updateRewardsForCompletedTask(currentUserId: String) {

        viewModelScope.launch {
            usersStorageService.incrementUserRewardPoints(currentUserId, 1,"TASK_COMPLETED_REWARD" )
        }
    }






    fun onAddClick(openScreen: (String) -> Unit) = openScreen(EDIT_TASK_SCREEN)


    fun onTaskActionClick(openScreen: (String) -> Unit, task: Task, action: String) {
        when (TaskActionOption.getByTitle(action)) {
            TaskActionOption.CompleteTask -> onTaskCompletedChange(task)
            TaskActionOption.EditTask -> openScreen("$EDIT_TASK_SCREEN?$TASK_ID={${task.id}}")
            TaskActionOption.DeleteTask -> onDeleteTaskChange(task)
        }
    }

    private fun onDeleteTaskChange(task: Task) {
        launchCatching {
            localTaskRepository.deleteTask(task)
            //storageService.delete(task.id)
        }
    }
}