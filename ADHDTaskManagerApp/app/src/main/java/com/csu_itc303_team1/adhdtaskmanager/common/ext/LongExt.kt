package com.csu_itc303_team1.adhdtaskmanager.common.ext

import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.SECONDS_PER_MINUTE
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.TimerState

fun Long.convertToTimerState(): TimerState {
    val min: Long = this / SECONDS_PER_MINUTE
    val remaining = (this - min * SECONDS_PER_MINUTE) / TimerState.SECOND_MILLS
    Log.d("convertToTimerState", "min: ${min}, remaining: $remaining")
    return TimerState(
        minutes = min.toInt(),
        seconds = remaining.toInt()
    )
}