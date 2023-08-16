package com.csu_itc303_team1.adhdtaskmanager.ui.pomodoro_timer

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import com.csu_itc303_team1.adhdtaskmanager.utils.permissions.isDoNotDisturbEnabled
import com.csu_itc303_team1.adhdtaskmanager.utils.permissions.toggleDoNotDisturb
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun PomodoroTimerScreen(
    initialWorkTime: Long,
    initialBreakTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 0f,
    strokeWidth: Dp = 20.dp,
    context: Context,
    activity: Activity
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var progress by remember { mutableFloatStateOf(initialValue) }
    var currentTime by remember { mutableLongStateOf(0L) }
    var timerRoundsCount by remember { mutableIntStateOf(4) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var isWorkTime by remember { mutableStateOf(false) }
    var isBreakTime by remember { mutableStateOf(true) }
    var seconds by remember { mutableIntStateOf(0) }
    var workTime by remember { mutableLongStateOf(initialWorkTime) }
    var breakTime by remember { mutableLongStateOf(initialBreakTime) }


    LaunchedEffect(key1 = isTimerRunning, key2 = currentTime) {
        if (currentTime > 0 && isTimerRunning) {
            delay(1000)
            currentTime -= 1000
            if (isWorkTime) {
                progress = currentTime.toFloat() / workTime.toFloat()
            } else if (isBreakTime) {
                progress = currentTime.toFloat() / breakTime.toFloat()
            }
            if (seconds == 0) {
                seconds = 59
            } else {
                seconds -= 1
            }
        } else if (currentTime == 0L && isTimerRunning) {
            if (isWorkTime) {
                timerRoundsCount -= 1
                isWorkTime = false
                isBreakTime = true
                currentTime = breakTime
            } else if (isBreakTime) {
                isWorkTime = true
                isBreakTime = false
                currentTime = workTime
            } else if (timerRoundsCount == 0) {
                isTimerRunning = false
                timerRoundsCount = 4
                currentTime = workTime
                isWorkTime = true
                isBreakTime = false
            }
        }
    }

    LaunchedEffect(key1 = isTimerRunning, key2 = isBreakTime) {
        if (isTimerRunning && isDoNotDisturbEnabled(context) && !isBreakTime) {
            toggleDoNotDisturb(context, activity)
        } else if (!isTimerRunning && !isDoNotDisturbEnabled(context) && isBreakTime) {
            toggleDoNotDisturb(context, activity)
        }
    }

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
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size = it },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress,
                modifier = modifier,
                strokeWidth = strokeWidth,
                strokeCap = StrokeCap.Round,
                color = if (isWorkTime) {
                    activeBarColor
                } else {
                    Color.Green
                }
            )
            Text(
                text = "%02d".format((currentTime / 1000L) / 60) + ":" + "%02d".format(seconds),
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
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(50)),
                shape =  MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = Color.White
                ),
                onClick = {
                    isTimerRunning = false
                    currentTime = workTime
                    isWorkTime = true
                    isBreakTime = false
                    timerRoundsCount = 4
                    seconds = 0
                    if (!isDoNotDisturbEnabled(context)) {
                        toggleDoNotDisturb(context, activity)
                    }
                }) {
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
                    if (currentTime <= 0L) {
                        currentTime = if (isWorkTime) {
                            workTime
                        } else {
                            breakTime
                        }
                        isTimerRunning = true

                    } else isTimerRunning = !isTimerRunning
                },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isTimerRunning || currentTime <= 0L) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    contentColor = if (!isTimerRunning || currentTime <= 0L) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.background
                    }
                )
            ) {
                Text(
                    text = if (!isTimerRunning && currentTime <= 0L) {
                        "Start"
                    } else if (isTimerRunning && currentTime >= 0L) {
                        "Pause"
                    } else {
                        "Resume"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}