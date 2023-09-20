package com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.time.ZoneId
import java.time.ZoneOffset

class AlarmSchedulerImpl(
    private val context: Context
): AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleAlarm(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", alarmItem.description)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmItem.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                alarmItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancelAlarm(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}