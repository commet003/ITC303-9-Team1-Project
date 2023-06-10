package com.csu_itc303_team1.adhdtaskmanager.pomodoro.circle

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimerDisplay(timerState: TimerState, toggleStartStop: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(width = 200.dp)
            .height(height = 200.dp)
    ) {
        CircularProgressIndicator(
            timerState.progressPercentage,
            Modifier.clickable { toggleStartStop() }
                .width(200.dp)
                .height(200.dp)
        )
        Text(
            text = timerState.displaySeconds,
            style = TextStyle(
                color = Color.White,
                fontSize = 40.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimerDisplay() {
    TimerDisplay(timerState = TimerState(240)) {

    }
}