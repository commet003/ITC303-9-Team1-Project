package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LeaderboardScreen() {


    val userList = leaderboardUsers()
    val sortedList = userList.sortedWith(compareByDescending { it.points })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        sortedList.forEach {
            LeaderboardItem(it,sortedList.indexOf(it) + 1)
        }
    }
}