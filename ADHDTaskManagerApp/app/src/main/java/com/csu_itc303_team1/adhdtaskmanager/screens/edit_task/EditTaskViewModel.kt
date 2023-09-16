package com.csu_itc303_team1.adhdtaskmanager.screens.edit_task



import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskRepository
import com.csu_itc303_team1.adhdtaskmanager.model.Alarm
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.AlarmService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
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
    private val alarmService: AlarmService,
    private val accountService: AccountService,
) : MainViewModel(logService) {

    private val currentUserId = accountService.currentUserId
    private var alarmHours: Int? = null
    private var alarmMinutes: Int? = null
    private var alarmInMillis: Long? = null

    @RequiresApi(Build.VERSION_CODES.O)
    val task = mutableStateOf(Task())

    init {
        val taskId = savedStateHandle.get<Int>(TASK_ID)
        if (taskId != null) {
            launchCatching {
                //task.value = storageService.getTask(taskId.idFromParameter()) ?: Task()
                task.value = localTaskRepository.getTaskByIdNonFlow(taskId) ?: Task()
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
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()))
        calendar.timeInMillis = newValue
        alarmInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        task.value = task.value.copy(dueDate = newDueDate)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        alarmHours = hour
        alarmMinutes = minute
        task.value = task.value.copy(dueTime = newDueTime)
    }

    fun onPriorityChange(newValue: Int) {
        task.value = task.value.copy(priority = newValue)
    }

    fun onCategoryChange(newCategory: String) {
        task.value = task.value.copy(category = newCategory)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    fun onDoneClick(popUpScreen: () -> Unit) {
        val date = Instant.ofEpochMilli(alarmInMillis ?: 0).atZone(ZoneId.systemDefault()).toLocalDate()
        val time = LocalTime.of(alarmHours ?: 0, alarmMinutes ?: 0)
        alarmInMillis = date.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        launchCatching {
            var editedTask = task.value
            editedTask = editedTask.copy(userId = currentUserId)
            if (editedTask.id != 0) {
                localTaskRepository.saveTask(editedTask)
                //storageService.save(editedTask)
            } else {
                localTaskRepository.saveTask(editedTask)
                //storageService.update(editedTask)
            }
            if (alarmInMillis != null) {
                val alarm = Alarm(
                    id = editedTask.id,
                    time = alarmInMillis!!,
                    title = editedTask.title,
                    description = editedTask.description,
                )
                alarmService.addAlarm(alarm)
            }
            Log.d("EditTaskViewModel", "Alarm: ${Instant.ofEpochMilli(alarmInMillis ?: 0).atZone(ZoneId.systemDefault()).toLocalDateTime()}")
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