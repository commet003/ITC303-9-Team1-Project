package com.csu_itc303_team1.adhdtaskmanager.utils.notifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

class NotificationsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(
            "PomodoroTimer",
            "Pomodoro Timer",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = "This permission needs to be turned on in order to receive" +
                " notifications for the Pomodoro Timer"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

    }
}