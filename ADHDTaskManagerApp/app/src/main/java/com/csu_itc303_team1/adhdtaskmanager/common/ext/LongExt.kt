package com.csu_itc303_team1.adhdtaskmanager.common.ext

import com.csu_itc303_team1.adhdtaskmanager.SECONDS_PER_MINUTE
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.TimerState

fun Long.convertToTimerState(): TimerState {
    val min: Long = this / SECONDS_PER_MINUTE
    val remaining = this - min * SECONDS_PER_MINUTE
    return TimerState(
        minutes = min,
        seconds = remaining / TimerState.SECOND_MILLS
    )
}