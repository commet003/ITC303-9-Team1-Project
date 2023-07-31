package com.csu_itc303_team1.adhdtaskmanager.utils.permissions

import android.app.Application
import android.app.AutomaticZenRule
import android.app.NotificationManager
import android.content.ComponentName
import android.net.Uri
import android.os.Build
import android.service.notification.ZenPolicy
import androidx.annotation.RequiresApi
import com.csu_itc303_team1.adhdtaskmanager.MainActivity

class DoNotDisturbApp: Application() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate() {
        super.onCreate()

    }

}