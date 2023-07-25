package com.csu_itc303_team1.adhdtaskmanager.pomodoro

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.csu_itc303_team1.adhdtaskmanager.MainActivity
import com.csu_itc303_team1.adhdtaskmanager.R

class ForegroundService : Service() {
    var timerStarted = false
    var bi = Intent(ForegroundService.COUNTDOWN_BR)

    private lateinit var timer : CountDownTimer

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val COUNTDOWN_BR = "ForegroundService.countdown_br"
    }

    override fun onCreate() {
        super.onCreate()
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (timerStarted) {
            return START_NOT_STICKY
        }

        try {
            timer = object: CountDownTimer(Integer.MAX_VALUE.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var msRemain: Long = millisUntilFinished
                    bi.putExtra("toCount", msRemain)
                    sendBroadcast(bi)
                }

                override fun onFinish() {

                }
            }
            timer.start()
            timerStarted = true
        } catch (err: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        createNotificationChannel()
        val notificationIntent = Intent(this, MainTimer::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer On, Lets Go")
                .setContentText("Hasta la vista, baby!")
                .setSmallIcon(R.drawable.timericon)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(9000, notification)
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        if (timerStarted) {
            timer.cancel()
        }
        bi.putExtra("forceStopped", true)
        sendBroadcast(bi)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

}