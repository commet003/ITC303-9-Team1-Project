package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.csu_itc303_team1.adhdtaskmanager.broadcast_receivers.AlarmReceiver
import com.csu_itc303_team1.adhdtaskmanager.model.AlarmItem
import com.csu_itc303_team1.adhdtaskmanager.model.service.AndroidAlarmSchedulerService
import java.time.ZoneId
import javax.inject.Inject

class AndroidAlarmSchedulerServiceImpl @Inject constructor(
    private val context: Context
): AndroidAlarmSchedulerService {


    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun schedule(alarmItem: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            /*TODO: Add a putExtra here for the application icon */
            putExtra("TASK_DETAILS", arrayOf(
                alarmItem.title,
                alarmItem.description,
                alarmItem.id,
                alarmItem.time.toString()
            ))
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmItem.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                    context,
                    alarmItem.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
        )
    }

    override fun cancel(alarmItem: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}