package com.csu_itc303_team1.adhdtaskmanager.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import com.csu_itc303_team1.adhdtaskmanager.MainActivity
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.CLICK_REQUEST_CODE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.PAUSE_REQUEST_CODE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.RESUME_REQUEST_CODE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.STOP_REQUEST_CODE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.TIMER_STATE


@ExperimentalAnimationApi
object ServiceHelper {

    private const val FLAG = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(TIMER_STATE, PomodoroTimerState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, FLAG
        )
    }

    fun stopPendingIntent(context: Context): PendingIntent {
        val stopIntent = Intent(context, PomodoroTimerService::class.java).apply {
            putExtra(TIMER_STATE, PomodoroTimerState.Stopped.name)
        }
        return PendingIntent.getService(
            context, PAUSE_REQUEST_CODE, stopIntent, FLAG
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, PomodoroTimerService::class.java).apply {
            putExtra(TIMER_STATE, PomodoroTimerState.Started.name)
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, FLAG
        )
    }

    fun cancelPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, PomodoroTimerService::class.java).apply {
            putExtra(TIMER_STATE, PomodoroTimerState.Canceled.name)
        }
        return PendingIntent.getService(
            context, STOP_REQUEST_CODE, cancelIntent, FLAG
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, PomodoroTimerService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}