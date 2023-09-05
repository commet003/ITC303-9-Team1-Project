package com.csu_itc303_team1.adhdtaskmanager.model

import com.csu_itc303_team1.adhdtaskmanager.data.LocalTask
import com.google.firebase.firestore.DocumentId
import java.util.UUID

data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val priority: String = "",
    val category: String = "None",
    val dueDate: String = "",
    val dueTime: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val userId: String = ""
){
    fun toLocalTask(): LocalTask {
        return LocalTask(
            id = id.ifBlank { UUID.randomUUID().toString() },
            title = title,
            priority = priority,
            category = category,
            dueDate = dueDate,
            dueTime = dueTime,
            description = description,
            completed = completed,
            userId = userId
        )
    }
}