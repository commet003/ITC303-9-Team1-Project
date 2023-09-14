package com.csu_itc303_team1.adhdtaskmanager.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.AndroidAlarmSchedulerServiceImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootCompletedReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            println("Boot completed")
            val alarmScheduler = AndroidAlarmSchedulerServiceImpl(context!!)
            Log.d("BootCompletedReceiver", "onReceive: Boot completed")
        }
    }

}