package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable


@Composable
fun PomodoroScreen(navigateBack: () -> Unit) {
    var workMinutes by remember { mutableStateOf("") }
    var breakMinutes by remember { mutableStateOf("") }
    var currentTimerValue by remember { mutableStateOf(0) }
    var isWorkTime by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .clickable { navigateBack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Pomodoro Timer")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            TextField(value = workMinutes, onValueChange = { workMinutes = it }, placeholder = { Text("Work minutes") })
            Spacer(modifier = Modifier.width(16.dp))
            TextField(value = breakMinutes, onValueChange = { breakMinutes = it }, placeholder = { Text("Break minutes") })
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "${currentTimerValue / 60}:${currentTimerValue % 60}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            coroutineScope.launch {
                val workTimeInSeconds = workMinutes.toIntOrNull() ?: 0 * 60
                val breakTimeInSeconds = breakMinutes.toIntOrNull() ?: 0 * 60

                for (i in workTimeInSeconds downTo 0) {
                    currentTimerValue = i
                    delay(1000L)
                }

                isWorkTime = false

                for (i in breakTimeInSeconds downTo 0) {
                    currentTimerValue = i
                    delay(1000L)
                }

                isWorkTime = true
            }
        }) {
            Text("Start")
        }
    }
}
