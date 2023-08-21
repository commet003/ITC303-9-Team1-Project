package com.csu_itc303_team1.adhdtaskmanager.ui.pomodoro_timer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.service.PomodoroTimerService
import com.csu_itc303_team1.adhdtaskmanager.service.PomodoroTimerState
import com.csu_itc303_team1.adhdtaskmanager.service.ServiceHelper
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_PAUSE
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_START
import com.csu_itc303_team1.adhdtaskmanager.utils.common.Constants.ACTION_SERVICE_STOP
import com.csu_itc303_team1.adhdtaskmanager.utils.common.formatTime


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PomodoroTimerScreen(
    pomodoroTimerService: PomodoroTimerService
) {

    val context = LocalContext.current
    val hours by pomodoroTimerService.hours
    val minutes by pomodoroTimerService.minutes
    val seconds by pomodoroTimerService.seconds
    val currentState by pomodoroTimerService.currentState



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ){
            Text(
                text = "Pomodoro Timer",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(250.dp),
                progress = 1f,
                strokeWidth = 10.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = formatTime(seconds = seconds, minutes = minutes, hours = hours),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .width(125.dp)
                    .height(125.dp)
                    .clip(RoundedCornerShape(50)),
                shape =  MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context, action = ACTION_SERVICE_STOP
                    )
                }
            ) {
                Text(
                    text = "Stop",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Button(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(50)),
                onClick = {
                    ServiceHelper.triggerForegroundService(
                        context = context,
                        action = if (currentState == PomodoroTimerState.Started) ACTION_SERVICE_PAUSE
                        else ACTION_SERVICE_START
                    )
                },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentState == PomodoroTimerState.Started) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (currentState == PomodoroTimerState.Started) "Pause"
                    else if ((currentState == PomodoroTimerState.Stopped)) "Resume"
                    else "Start",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}
