package com.csu_itc303_team1.adhdtaskmanager

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Todo(
    val title: String,
    val description: String,
    val priority: Priority,
    val dueDate: String,
    val dueTime: String,
    val isCompleted: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)