package com.csu_itc303_team1.adhdtaskmanager.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTask
import com.google.firebase.firestore.DocumentId
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val priority: Int = 0,
    val category: String = "None",
    val dueDate: String = "",
    val dueTime: String = "",
    var reminderSet: Boolean = false,
    var taskReminderTime: String = Instant.now().toString(),
    val description: String = "",
    val completed: Boolean = false,
    val userId: String = ""
){
    fun toLocalTask(): LocalTask {
        return LocalTask(
            id = 0,
            title = title,
            priority = priority,
            category = category,
            dueDate = dueDate,
            dueTime = dueTime,
            reminderSet = reminderSet,
            taskReminderTime = taskReminderTime,
            description = description,
            completed = completed,
            userId = userId
        )
    }
}