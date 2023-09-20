package com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        Log.d("AlarmReceiver", message)
    }

}