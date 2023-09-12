package com.csu_itc303_team1.adhdtaskmanager.screens.pomodoro_timer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.sharp.Computer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(

) : ViewModel() {

    val pomCount = MutableLiveData(4)

    private val _sessionDuration: MutableLiveData<Int> = MutableLiveData()
    val sessionDuration: LiveData<Int> = _sessionDuration

    private var currentSessionType: SessionState = SessionState.FOCUS

    private val _completedPom: MutableLiveData<Int> = MutableLiveData()
    val completedPom: LiveData<Int> = _completedPom

    //expose duration to view
    private val _indicators: MutableLiveData<List<SessionIndicatorEntity>> = MutableLiveData()
    val indicators: LiveData<List<SessionIndicatorEntity>> = _indicators

    fun subscribe() {
        // use last session type that user was in
        updateSessionType(SessionState.FOCUS)
        getIndicators()
    }

    private fun getIndicators() {
        val indicators = arrayListOf<SessionIndicatorEntity>()
        viewModelScope.launch {
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Focus",
                        duration = sessionDuration.value ?: 0,
                        icon = Icons.Sharp.Computer,
                        active = currentSessionType == SessionState.FOCUS
                    )
                )
                _indicators.postValue(indicators)
        }
        viewModelScope.launch {
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Short Break",
                        duration = sessionDuration.value ?: 0,
                        icon = Icons.Filled.LocalCafe,
                        active = currentSessionType == SessionState.SHORT_BREAK
                    )
                )
                _indicators.postValue(indicators)
        }
        viewModelScope.launch {
                indicators.add(
                    SessionIndicatorEntity(
                        title = "Long Break",
                        duration = sessionDuration.value ?: 0,
                        icon = Icons.Filled.Chair,
                        active = currentSessionType == SessionState.LONG_BREAK
                    )
                )
                _indicators.postValue(indicators)
        }
    }

    fun onSessionCompleted() {
        when (currentSessionType) {
            SessionState.FOCUS -> onFocusFinish()
            SessionState.LONG_BREAK -> onLongBreakFinish()
            else -> onShortBreakFinish()
        }
    }

    private fun onFocusFinish() {
        viewModelScope.launch {
                val currentPomState = _completedPom.value ?: 0
                _completedPom.value = currentPomState + 1
                if (currentPomState + 1 == pomCount.value) {
                    updateSessionType(SessionState.LONG_BREAK)
                } else {
                    updateSessionType(SessionState.SHORT_BREAK)
                }
        }
    }

    private fun onShortBreakFinish() {
        updateSessionType(SessionState.FOCUS)
    }

    private fun onLongBreakFinish() {
        viewModelScope.launch {
                _completedPom.value = 0
        }
        updateSessionType(SessionState.FOCUS)
    }

    private fun updateSessionType(type: SessionState) {
        currentSessionType = type
        getIndicators()
        val session = when (currentSessionType) {
            SessionState.FOCUS -> 25
            SessionState.LONG_BREAK -> 15
            else -> 5
        }
        viewModelScope.launch {
            _sessionDuration.postValue(session)
        }
    }
}