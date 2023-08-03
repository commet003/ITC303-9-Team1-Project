package com.csu_itc303_team1.adhdtaskmanager.utils.permissions

import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


fun getPermissions(context: Context) {
    Dexter.withContext(context)
        .withPermissions(
            android.Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            android.Manifest.permission.WRITE_SETTINGS,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report.let {
                    if (report?.areAllPermissionsGranted() == true) {
                        Toast.makeText(
                            context,
                            "All permissions granted",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Permissions not granted",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).withErrorListener {
            Toast.makeText(
                context,
                "Error occurred" + it.name,
                Toast.LENGTH_LONG
            ).show()
        }.check()
}

fun setDoNotDisturb(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (notificationManager.currentInterruptionFilter != NotificationManager.INTERRUPTION_FILTER_NONE) {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
    } else {
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
    }
}