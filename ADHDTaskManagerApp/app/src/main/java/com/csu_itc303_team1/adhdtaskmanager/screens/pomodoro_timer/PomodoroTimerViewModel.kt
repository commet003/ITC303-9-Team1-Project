package com.csu_itc303_team1.adhdtaskmanager.screens.pomodoro_timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csu_itc303_team1.adhdtaskmanager.model.service.PomodoroTimerService
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.TimerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroTimerViewModel @Inject constructor(
    private val timeManager: PomodoroTimerService,
) : ViewModel() {

    private val _buttonState: MutableLiveData<Boolean> = MutableLiveData()
    val timerRunning: LiveData<Boolean> = _buttonState

    private var sessionTimerState = TimerState()

    val progress: LiveData<Float> = timeManager.progress
    val timer: LiveData<TimerState> = timeManager.timer

    fun subscribe(duration: Int) {
        sessionTimerState = TimerState(duration, 0)
        timeManager.initiate(sessionTimerState)
    }

    fun startTimer(onTimerCompleted: () -> Unit) {
        _buttonState.value = true
        timeManager.startTimer(sessionTimerState) {
            onTimerCompleted()
            timeManager.pauseTimer()
            _buttonState.value = false
        }
    }

    fun pauseTimer() {
        _buttonState.value = false
        timeManager.pauseTimer()
    }

    fun restartTimer() {
        _buttonState.value = false
        timeManager.restartTimer(sessionTimerState)
    }

}