package com.csu_itc303_team1.adhdtaskmanager.model

import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId val id: String = "",
    val title: String = "",
    val priority: String = "Low",
    val category: String = "Home",
    val dueDate: String = "",
    val dueTime: String = "",
    val description: String = "",
    val completed: Boolean = false,
    val userId: String = ""
)