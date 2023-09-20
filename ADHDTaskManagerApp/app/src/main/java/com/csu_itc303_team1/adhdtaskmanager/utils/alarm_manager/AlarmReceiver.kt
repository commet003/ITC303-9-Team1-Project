package com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.time.LocalDateTime

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("EXTRA_TITLE") ?: return
        val description = intent?.getStringExtra("EXTRA_DESCRIPTION") ?: return
        val time = intent.getLongExtra("EXTRA_TIME", 0)
        Log.d("AlarmReceiver", title)
        Log.d("AlarmReceiver", description)
        Log.d("AlarmReceiver", LocalDateTime.ofEpochSecond(time, 0, null).toString())
    }

}