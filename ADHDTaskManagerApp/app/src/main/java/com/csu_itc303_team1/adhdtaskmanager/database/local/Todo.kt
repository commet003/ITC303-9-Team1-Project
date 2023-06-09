package com.csu_itc303_team1.adhdtaskmanager.database.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.csu_itc303_team1.adhdtaskmanager.Priority


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
) {
    fun markAsCompleted(): Todo {
            return copy(isCompleted = true)
        }
}