package com.csu_itc303_team1.adhdtaskmanager.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.AlarmManagerCompat
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID_EXTRA
import java.time.ZoneId
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun AlarmManager.scheduleAlarm(alarmItem: AlarmItem, context: Context) {

    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra(TASK_ID_EXTRA, alarmItem.id)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmItem.id,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    AlarmManagerCompat.setExactAndAllowWhileIdle(this, AlarmManager.RTC_WAKEUP, alarmItem.time.toEpochSecond(
        ZoneId.systemDefault() as ZoneOffset?
    ), pendingIntent)
}

fun AlarmManager.cancelAlarm(alarmId: Int, context: Context) {

    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        alarmId,
        intent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )
    cancel(pendingIntent)
}