package com.csu_itc303_team1.adhdtaskmanager.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val priority: String = "",
    val category: String = "",
    val dueDate: String = "",
    val dueTime: String = "",
    val description: String = "",
    val url: String = "",
    val flag: Boolean = false,
    val completed: Boolean = false,
    val userId: String = ""
)