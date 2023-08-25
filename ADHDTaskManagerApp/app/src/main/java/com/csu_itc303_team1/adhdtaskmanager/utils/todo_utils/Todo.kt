package com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties


enum class SortType {
    BY_PRIORITY,
    BY_DATE_TIME,
    BY_COMPLETED,
    BY_NOT_COMPLETED
}

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

@Entity
@IgnoreExtraProperties
data class Todo(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "priority") var priority: Priority,
    @ColumnInfo(name = "due_date") var dueDate: String,
    @ColumnInfo(name = "due_time") var dueTime: String,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "completion_date") var completionDate: String = "",
    @ColumnInfo(name = "is_clicked") var isClicked: Boolean = false,
    @ColumnInfo(name = "userId") var userId: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "description" to description,
            "priority" to priority,
            "dueDate" to dueDate,
            "dueTime" to dueTime,
            "isCompleted" to isCompleted,
            "completionDate" to completionDate,
            "isClicked" to isClicked,
            "id" to id,
            )
    }
}