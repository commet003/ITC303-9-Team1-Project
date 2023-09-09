package com.csu_itc303_team1.adhdtaskmanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class LocalTask(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "task_title") var title: String = "",
    @ColumnInfo(name = "task_description")var description: String = "",
    @ColumnInfo(name = "task_priority")var priority: Int = 0,
    @ColumnInfo(name = "task_category")var category: String = "",
    @ColumnInfo(name = "task_dueDate")var dueDate: String = "",
    @ColumnInfo(name = "task_dueTime")var dueTime: String = "",
    @ColumnInfo(name = "task_completed")var completed: Boolean = false,
    @ColumnInfo(name = "task_userId")var userId: String = ""
)