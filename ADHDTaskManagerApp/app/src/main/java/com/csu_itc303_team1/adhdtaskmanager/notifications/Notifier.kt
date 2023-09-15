package com.csu_itc303_team1.adhdtaskmanager.notifications

import com.csu_itc303_team1.adhdtaskmanager.model.Task

interface Notifier {
    fun postTasksNotifications(tasks: List<Task>)
    fun postTaskNotification(title: String, description: String)
}