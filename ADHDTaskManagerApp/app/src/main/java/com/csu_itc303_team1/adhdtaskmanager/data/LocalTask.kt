package com.csu_itc303_team1.adhdtaskmanager.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Entity(tableName = "task_table")
data class LocalTask @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "task_title") var title: String = "",
    @ColumnInfo(name = "task_description")var description: String = "",
    @ColumnInfo(name = "task_priority")var priority: Int = 0,
    @ColumnInfo(name = "task_category")var category: String = "",
    @ColumnInfo(name = "task_dueDate")var dueDate: String = "",
    @ColumnInfo(name = "task_dueTime")var dueTime: String = "",
    @ColumnInfo(name = "is_reminer_set")var reminderSet: Boolean = false,
    @ColumnInfo(name = "task_reminder_time_date")var taskReminderTime: String = Instant.now().toString(),
    @ColumnInfo(name = "task_completed")var completed: Boolean = false,
    @ColumnInfo(name = "task_userId")var userId: String = ""
)