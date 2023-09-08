package com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity
data class Todo(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "todo_title") var title: String = "",
    @ColumnInfo(name = "todo_description")var description: String = "",
    @ColumnInfo(name = "todo_priority")var priority: Int = 0,
    @ColumnInfo(name = "todo_category")var category: String = "",
    @ColumnInfo(name = "todo_dueDate")var dueDate: String = "",
    @ColumnInfo(name = "todo_dueTime")var dueTime: String = "",
    @ColumnInfo(name = "todo_isClicked")var isClicked: Boolean = false,
    @ColumnInfo(name = "todo_completed")var completed: Boolean = false,
    @ColumnInfo(name = "todo_completedDate")var completedDate: String = "",
    @ColumnInfo(name = "todo_userId")var userId: String = ""
)