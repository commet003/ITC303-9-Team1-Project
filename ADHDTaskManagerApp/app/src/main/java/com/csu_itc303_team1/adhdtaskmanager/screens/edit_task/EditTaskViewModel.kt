package com.csu_itc303_team1.adhdtaskmanager.screens.edit_task



import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskRepository
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class EditTaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val localTaskRepository: LocalTaskRepository,
    private val accountService: AccountService,
) : MainViewModel(logService) {

    private val currentUserId = accountService.currentUserId

    @RequiresApi(Build.VERSION_CODES.O)
    val task = mutableStateOf(Task())

    init {
        val taskId = savedStateHandle.get<String>(TASK_ID)
        if (taskId != null) {
            launchCatching {
                //task.value = storageService.getTask(taskId.idFromParameter()) ?: Task()
                localTaskRepository.getTaskById(taskId).mapLatest {
                    task.value = it
                }
            }
        }
    }

    fun onTitleChange(newValue: String) {
        task.value = task.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        task.value = task.value.copy(description = newValue)
    }


    fun onDateChange(newValue: Long) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
        calendar.timeInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        task.value = task.value.copy(dueDate = newDueDate)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        task.value = task.value.copy(dueTime = newDueTime)
    }

    fun onPriorityChange(newValue: Int) {
        task.value = task.value.copy(priority = newValue)
    }

    fun onCategoryChange(newCategory: String) {
        task.value = task.value.copy(category = newCategory)
    }


    fun onDoneClick(popUpScreen: () -> Unit) {
        launchCatching {
            var editedTask = task.value
            editedTask = editedTask.copy(userId = currentUserId)
            if (editedTask.id != 0L) {
                localTaskRepository.saveTask(editedTask)
                //storageService.save(editedTask)
            } else {
                localTaskRepository.saveTask(editedTask)
                //storageService.update(editedTask)
            }
            Log.d("Edited Task", "Edited Task: $editedTask")
            popUpScreen()
        }
    }

    private fun Int.toClockPattern(): String {
        return if (this < 10) "0$this" else "$this"
    }

    companion object {
        private const val UTC = "UTC"
        private const val DATE_FORMAT = "EEE, dd/MM/yyyy"
    }
}