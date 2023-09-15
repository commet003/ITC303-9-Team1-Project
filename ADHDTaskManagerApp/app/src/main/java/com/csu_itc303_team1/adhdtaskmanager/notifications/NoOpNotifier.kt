package com.csu_itc303_team1.adhdtaskmanager.notifications

import com.csu_itc303_team1.adhdtaskmanager.model.Task
import javax.inject.Inject

/**
 * Implementation of [Notifier] which does nothing. Useful for tests and previews.
 */
class NoOpNotifier @Inject constructor() : Notifier {
    override fun postTasksNotifications(tasks: List<Task>) = Unit
    override fun postTaskNotification(title: String, description: String) = Unit
}