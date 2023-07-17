package com.csu_itc303_team1.adhdtaskmanager

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Todo(
    var title: String,
    var description: String,
    var priority: Priority,
    var dueDate: String,
    var dueTime: String,
    val isCompleted: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)