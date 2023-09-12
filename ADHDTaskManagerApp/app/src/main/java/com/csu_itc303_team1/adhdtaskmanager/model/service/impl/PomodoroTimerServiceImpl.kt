package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csu_itc303_team1.adhdtaskmanager.common.ext.convertToTimerState
import com.csu_itc303_team1.adhdtaskmanager.model.service.PomodoroTimerService
import javax.inject.Inject


data class PomodoroScreenEntity(
    val sessionName: String,
    val text: String,
    val numberOfPoms: Int,
    val pomsCompleted: Int,
    val progress: Float,
    val timerRunning: Boolean,
)

enum class SessionState {
    FOCUS,
    SHORT_BREAK,
    LONG_BREAK
}

data class TimerState(
    val minutes: Long = 0,
    val seconds: Long = 0
) {

    companion object {
        const val SECOND_MILLS = 100L
        const val SECONDS_PER_MINUTE = 6000L
    }

    fun convertToSeconds(): Long {
        return minutes * SECONDS_PER_MINUTE + seconds * SECOND_MILLS
    }

    override fun toString(): String {
        return "%02d".format(minutes) + ":" + "%02d".format(seconds)
    }
}



class PomodoroTimerServiceImpl @Inject constructor(): PomodoroTimerService {
    private val _progress: MutableLiveData<Float> = MutableLiveData()
    override val progress: LiveData<Float> = _progress

    override val _timer: MutableLiveData<TimerState> = MutableLiveData()
    override val timer: LiveData<TimerState> = _timer

    override var currentTimerState = TimerState()

    private var countDownTimer: CountDownTimer? = null

    override fun initiate(session: TimerState) {
        currentTimerState = session
        updateTimerSate(currentTimerState, session)
    }

    override fun startTimer(session: TimerState, onSessionCompleted: () -> Unit) {
        if (countDownTimer != null) return
        countDownTimer = object : CountDownTimer(
            currentTimerState.convertToSeconds(),
            TimerState.SECOND_MILLS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerSate(millisUntilFinished.convertToTimerState(), session)
            }

            override fun onFinish() {
                updateTimerSate(session, session)
                onSessionCompleted()
            }
        }.start()
    }

    override fun pauseTimer() {
        _timer.value?.apply {
            currentTimerState = TimerState(
                minutes = minutes,
                seconds = seconds
            )
        }
        countDownTimer?.cancel()
        countDownTimer = null
    }

    override fun restartTimer(session: TimerState) {
        pauseTimer()
        currentTimerState = session
        updateTimerSate(currentTimerState, session)
    }

    override fun updateProgress(current: TimerState, session: TimerState): Float {
        val total = session.convertToSeconds()
        val remaining = total - current.convertToSeconds()
        if (total == 0L) return 0f
        return remaining.toFloat() / total.toFloat()
    }

    override fun updateTimerSate(current: TimerState, session: TimerState) {
        _timer.value = current
        _progress.value = updateProgress(current, session)
    }
}