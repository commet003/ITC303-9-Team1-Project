package com.csu_itc303_team1.adhdtaskmanager.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_IDLE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_PAUSE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_START
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_STOP
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.NOTIFICATION_CHANNEL_ID
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.NOTIFICATION_CHANNEL_NAME
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.NOTIFICATION_ID
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.TIMER_STATE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.formatTime
import com.csu_itc303_team1.adhdtaskmanager.utils.common.pad
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ExperimentalAnimationApi
@AndroidEntryPoint
class PomodoroTimerService : Service() {
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val binder = PomodoroTimerBinder()

    private var duration: Duration = Duration.ZERO
    private lateinit var timer: Timer

    var seconds = mutableStateOf("00")
        private set
    var minutes = mutableStateOf("00")
        private set
    var hours = mutableStateOf("00")
        private set
    var currentState = mutableStateOf(PomodoroTimerState.Idle)
        private set

    override fun onBind(p0: Intent?) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(TIMER_STATE)) {
            PomodoroTimerState.Started.name -> {
                setPauseButton()
                //startForegroundService()
                startStopwatch { hours, minutes, seconds ->
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
            }
            PomodoroTimerState.Stopped.name -> {
                stopStopwatch()
                setResumeButton()
            }
            PomodoroTimerState.Canceled.name -> {
                stopStopwatch()
                cancelStopwatch()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_START -> {
                    setPauseButton()
                    startForegroundService()
                    startStopwatch { hours, minutes, seconds ->
                        updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                    }
                }
                ACTION_SERVICE_PAUSE -> {
                    stopStopwatch()
                    setResumeButton()
                }
                ACTION_SERVICE_STOP -> {
                    stopStopwatch()
                    cancelStopwatch()
                    stopForegroundService()
                }
                ACTION_SERVICE_IDLE -> {
                    startForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        currentState.value = PomodoroTimerState.Started
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState.value = PomodoroTimerState.Stopped
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        currentState.value = PomodoroTimerState.Idle
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            this@PomodoroTimerService.hours.value = hours.toInt().pad()
            this@PomodoroTimerService.minutes.value = minutes.pad()
            this@PomodoroTimerService.seconds.value = seconds.pad()
        }
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationManager.notify(
            NOTIFICATION_ID,
            notificationBuilder.setContentText(
                formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                )
            ).build()
        )
    }

    @SuppressLint("RestrictedApi")
    private fun setPauseButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Pause",
                ServiceHelper.stopPendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    @SuppressLint("RestrictedApi")
    private fun setResumeButton() {
        notificationBuilder.mActions.removeAt(0)
        notificationBuilder.mActions.add(
            0,
            NotificationCompat.Action(
                0,
                "Resume",
                ServiceHelper.resumePendingIntent(this)
            )
        )
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    inner class PomodoroTimerBinder : Binder() {
        fun getService(): PomodoroTimerService = this@PomodoroTimerService
    }
}

enum class PomodoroTimerState {
    Idle,
    Started,
    Stopped,
    Canceled
}