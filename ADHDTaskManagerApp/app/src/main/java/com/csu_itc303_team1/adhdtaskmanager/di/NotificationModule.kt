package com.csu_itc303_team1.adhdtaskmanager.di

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.service.ServiceHelper
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@ExperimentalAnimationApi
@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        Log.d("NotificationModule", "provideNotificationBuilder: ")
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Pomodoro Timer")
            .setContentText("00:00")
            .setSmallIcon(R.drawable.ic_baseline_timer_24)
            .setOngoing(true)
            .addAction(0, "Start", ServiceHelper.startPendingIntent(context))
            .addAction(0, "Stop", ServiceHelper.cancelPendingIntent(context))
            .setContentIntent(ServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}