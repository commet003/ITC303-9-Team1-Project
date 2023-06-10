package com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow

class TimerVm : ViewModel() {
    private val timerIntent = TimerUseCase(viewModelScope)
    val timerStateFlow: StateFlow<TimerState> = timerIntent.timerStateFlow

    fun toggleStart() = timerIntent.toggleTime(600)
}

data class TimerState(
    val secondsRemaining: Int? = null,
    val totalSeconds: Int = 600,
    val textWhenStopped: String = "-"
) {
    val displaySeconds: String = (secondsRemaining ?: textWhenStopped).toString()
    val progressPercentage: Float =
        (secondsRemaining ?: totalSeconds) / totalSeconds.toFloat()

    override fun toString(): String = "Seconds Remaining $secondsRemaining, totalSeconds: $totalSeconds, progress: $progressPercentage"
}