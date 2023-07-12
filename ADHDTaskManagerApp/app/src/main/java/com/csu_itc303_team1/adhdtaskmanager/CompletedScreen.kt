package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDateTime

@Composable
fun CompletedScreen() {

    var completedTasks by remember{
        mutableStateOf(LocalDateTime.MIN)
    }

    Column(){
        Button(onClick = {
            completedTasks = LocalDateTime.now()

    }){
            Text("Click For Current Time & Date")
        }

        Text(
            text = completedTasks.toString()
        )
    }


}