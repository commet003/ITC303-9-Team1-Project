package com.csu_itc303_team1.adhdtaskmanager.broadcast_receivers

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.csu_itc303_team1.adhdtaskmanager.TASK_ID_EXTRA
import com.csu_itc303_team1.adhdtaskmanager.utils.GetTaskById
import com.csu_itc303_team1.adhdtaskmanager.utils.sendNotification
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {

    @Inject
    lateinit var getTaskById: GetTaskById

    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context?, intent: Intent?) {
        runBlocking {
            val task = intent?.getIntExtra(TASK_ID_EXTRA, 0)?.let { getTaskById(it) }
            task?.let {
                val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.sendNotification(task, context, task.id)
            }
        }
    }
}