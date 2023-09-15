package com.csu_itc303_team1.adhdtaskmanager.model.service

import androidx.lifecycle.LiveData
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.TimerState

interface PomodoroTimerService{
    val timer: LiveData<TimerState>
    var currentTimerState: TimerState
    val progress: LiveData<Float>
    fun initiate(session: TimerState)
    fun startTimer(session: TimerState, onSessionCompleted: () -> Unit)
    fun pauseTimer()
    fun restartTimer(session: TimerState)
    fun updateProgress(current: TimerState, session: TimerState): Float
    fun updateTimerSate(current: TimerState, session: TimerState)

}