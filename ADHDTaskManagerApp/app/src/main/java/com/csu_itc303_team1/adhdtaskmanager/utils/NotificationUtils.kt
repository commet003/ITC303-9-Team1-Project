package com.csu_itc303_team1.adhdtaskmanager.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.csu_itc303_team1.adhdtaskmanager.EDIT_TASK_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.MainActivity
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_CHANNEL_ID
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTask
import com.csu_itc303_team1.adhdtaskmanager.model.Priority

@OptIn(ExperimentalMaterialApi::class)
fun NotificationManager.sendNotification(task: LocalTask, context: Context, id: Int) {

    val taskDetailIntent = Intent(
        Intent.ACTION_VIEW,
        "${EDIT_TASK_SCREEN}/${task.id}".toUri(),
        context,
        MainActivity::class.java
    )
    val taskDetailsPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(taskDetailIntent)
        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val notification = NotificationCompat.Builder(context, TASK_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_calendar)
        .setContentTitle(task.title)
        .setContentText(task.description)
        .setContentIntent(taskDetailsPendingIntent)
        .setPriority(
            when (task.priority) {
                Priority.None.value -> NotificationCompat.PRIORITY_DEFAULT
                Priority.Low.value -> NotificationCompat.PRIORITY_LOW
                Priority.Medium.value -> NotificationCompat.PRIORITY_HIGH
                Priority.High.value -> NotificationCompat.PRIORITY_MAX
                else -> NotificationCompat.PRIORITY_DEFAULT
            }
        )
        .build()

    notify(id, notification)
}