package com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TimerUseCase(private val timerScope: CoroutineScope) {
    private var _timerStateFlow = MutableStateFlow(TimerState())
    val timerStateFlow: StateFlow<TimerState> = _timerStateFlow

    private var job: Job? = null

    fun toggleTime(totalSeconds: Int) {
        if (job == null) {
            job = timerScope.launch {
                initTimer(totalSeconds)
                    .onCompletion { _timerStateFlow.emit(TimerState()) }
                    .collect { _timerStateFlow.emit(it) }
            }
        } else {
            job?.cancel()
            job = null
        }
    }

    private fun initTimer(totalSeconds: Int): Flow<TimerState> =
        (totalSeconds - 1 downTo 0).asFlow()
            .onEach { delay(1000) }
            .onStart { emit(totalSeconds) }
            .conflate()
            .transform { remainingSeconds: Int -> emit(TimerState(remainingSeconds)) }

}