package com.csu_itc303_team1.adhdtaskmanager.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "task_table")
@RequiresApi(Build.VERSION_CODES.O)
data class Task(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
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
)