package com.csu_itc303_team1.adhdtaskmanager.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import com.csu_itc303_team1.adhdtaskmanager.MAX_NUM_NOTIFICATIONS
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.TARGET_ACTIVITY_NAME
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_CHANNEL_ID
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_GROUP
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_ID
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_REQUEST_CODE
import com.csu_itc303_team1.adhdtaskmanager.TASK_NOTIFICATION_SUMMARY_ID
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {

    @SuppressLint("StringFormatInvalid")
    override fun postTasksNotifications(
        tasks: List<Task>,
    ) = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val truncatedTasks = tasks
            .take(MAX_NUM_NOTIFICATIONS)

        val taskNotifications = truncatedTasks
            .map { task ->
                createTaskNotification {
                    setSmallIcon(R.drawable.ic_calendar)
                        .setContentTitle(task.title)
                        .setContentText(task.description)
                        .setContentIntent(taskPendingIntent(task))
                        .setGroup(TASK_NOTIFICATION_GROUP)
                        .setAutoCancel(true)
                }
            }
        val summaryNotification = createTaskNotification {
            val title = getString(
                R.string.task_notification_group_summary,
                truncatedTasks.size,
            )
            setContentTitle(title)
                .setContentText(title)
                .setSmallIcon(R.drawable.ic_calendar)
                // Build summary info into InboxStyle template.
                .setStyle(tasksNotificationStyle(truncatedTasks, title))
                .setGroup(TASK_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build()
        }

        // Send the notifications
        val notificationManager = NotificationManagerCompat.from(this)
        taskNotifications.forEachIndexed { index, notification ->
            notificationManager.notify(
                truncatedTasks[index].id.hashCode(),
                notification,
            )
        }
        notificationManager.notify(TASK_NOTIFICATION_SUMMARY_ID, summaryNotification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun postTaskNotification(title: String, description: String) = with(context){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val taskNotification = createTaskNotification {
            setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(taskPendingIntent(Task()))
                .setGroup(TASK_NOTIFICATION_GROUP)
        }
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(TASK_NOTIFICATION_ID, taskNotification)
    }

    /**
     * Creates an inbox style summary notification for news updates
     */
    private fun tasksNotificationStyle(
        tasks: List<Task>,
        title: String,
    ): NotificationCompat.Style = tasks
        .fold(InboxStyle()) { inboxStyle, task ->
            inboxStyle.addLine(task.title)
        }
        .setBigContentTitle(title)
        .setSummaryText(title)
}

/**
 * Creates a notification for configured for news updates
 */
private fun Context.createTaskNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        TASK_NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //.apply(block)
        .build()
}

/**
 * Ensures the a notification channel is is present if applicable
 */
private fun Context.ensureNotificationChannelExists() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val channel = NotificationChannel(
        TASK_NOTIFICATION_CHANNEL_ID,
        getString(R.string.task_notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.task_notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.taskPendingIntent(
    task: Task,
): PendingIntent? = PendingIntent.getActivity(
    this,
    TASK_NOTIFICATION_REQUEST_CODE,
    Intent().apply {
        action = Intent.ACTION_VIEW
        component = ComponentName(
            packageName,
            TARGET_ACTIVITY_NAME,
        )
    },
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
)