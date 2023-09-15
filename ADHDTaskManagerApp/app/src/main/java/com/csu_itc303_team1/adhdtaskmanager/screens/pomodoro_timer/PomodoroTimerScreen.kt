package com.csu_itc303_team1.adhdtaskmanager.screens.pomodoro_timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.common.composable.ControlButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.PomodoroTimer
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferences
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.PomodoroScreenEntity

@Composable
fun Timer(
    viewModel: PomodoroTimerViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val preferences = sessionViewModel.getPreferences().collectAsState(initial = UserPreferences())
    viewModel.subscribe(sessionViewModel.sessionDuration.value ?: preferences.value.pomodoroTimerFocusDuration.toInt())
    Timer(
        viewModel,
        sessionViewModel,
    ) { sessionViewModel.onSessionCompleted() }
}

@Composable
internal fun Timer(
    viewModel: PomodoroTimerViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel(),
    onTimerCompleted: () -> Unit
) {
    val timerPreferences by sessionViewModel.getPreferences().collectAsState(initial = UserPreferences())
    val timerState = viewModel.timer.observeAsState()
    val timerRunning = viewModel.timerRunning.observeAsState()
    val progress = viewModel.progress.observeAsState()
    PomScreen(
        modifier = Modifier,
        PomodoroScreenEntity(
            sessionName = "Pomodoro Timer",
            text = timerState.value.toString(),
            numberOfPoms = timerPreferences.pomodoroTimerRounds,
            pomsCompleted = 0,
            timerRunning = timerRunning.value ?: false,
            progress = progress.value ?: 0f,
        ),
        { viewModel.startTimer(onTimerCompleted) },
        viewModel::pauseTimer,
        viewModel::restartTimer,
    )
}

@Composable
fun PomScreen(
    modifier: Modifier = Modifier,
    entity: PomodoroScreenEntity,
    onStartClicked: () -> Unit,
    onPauseClicked: () -> Unit,
    onRestartClicked: () -> Unit,
    viewModel: PomodoroTimerViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = entity.sessionName,
                modifier = Modifier.padding(vertical = 32.dp),
                fontSize = 25.sp
            )
        }
        Box {
            PomodoroTimer(
                text = entity.text,
                progress = entity.progress,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .aspectRatio(1f)
            )
        }
        ControlButton(
            running = entity.timerRunning,
            onResumeClicked = onStartClicked,
            onRestartClicked = onRestartClicked,
            onPauseClicked = onPauseClicked,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SessionIcon(
    title: String,
    imageVector: ImageVector,
    duration: String,
    tint: Color,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = duration, fontSize = MaterialTheme.typography.bodySmall.fontSize, color = textColor)
        Icon(
            imageVector = imageVector,
            tint = tint,
            contentDescription = "",
            modifier = modifier.size(25.dp)
        )
        Text(text = title, fontSize = MaterialTheme.typography.bodySmall.fontSize, color = textColor)
    }
}


data class SessionIndicatorEntity(
    val title: String,
    val icon: ImageVector,
    val duration: Int,
    val active: Boolean,
)

@Composable
fun SessionIndicator(
    indicators: List<SessionIndicatorEntity>,
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val active = MaterialTheme.colorScheme.primary
    val inactive = MaterialTheme.colorScheme.primaryContainer

    val activeTextColor = MaterialTheme.colorScheme.onSurface
    val inactiveTextColor = MaterialTheme.colorScheme.onSecondary
    val preferences = viewModel.getPreferences().collectAsState(initial = UserPreferences())

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        indicators.forEach { indicator ->
            Column(
                modifier = modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textColor = if (indicator.active) activeTextColor else inactiveTextColor
                val tint = if (indicator.active) active else inactive
                Text(
                    text = when(indicator){
                        indicators[0] -> "${preferences.value.pomodoroTimerFocusDuration} Mins"
                        indicators[1] -> "${preferences.value.pomodoroTimerShortBreakDuration} Mins"
                        indicators[2] -> "${preferences.value.pomodoroTimerLongBreakDuration} Mins"
                        else -> ""
                    },
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = textColor
                )
                Icon(
                    imageVector = indicator.icon,
                    tint = tint,
                    contentDescription = "",
                    modifier = modifier.size(25.dp)
                )
                Text(text = indicator.title, fontSize = MaterialTheme.typography.bodySmall.fontSize, color = textColor)
            }
        }
    }
}


@Composable
fun Session(
    viewModel: SessionViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    viewModel.subscribe()
    SessionScreen(viewModel, modifier)
}

@Composable
internal fun SessionScreen(
    viewModel: SessionViewModel,
    modifier: Modifier = Modifier
) {
    val preferences = viewModel.getPreferences().collectAsState(initial = UserPreferences())
    Column(
        modifier = modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val completed = viewModel.completedPom.observeAsState(initial = 0).value
        Text(
            text = "$completed of ${preferences.value.pomodoroTimerRounds}",
            modifier = Modifier.padding(vertical = 16.dp),
            fontSize = 20.sp
        )

        SessionIndicator(viewModel.indicators.observeAsState(listOf()).value)
    }
}