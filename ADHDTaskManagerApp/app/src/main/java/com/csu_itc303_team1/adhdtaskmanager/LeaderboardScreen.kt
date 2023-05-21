package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LeaderboardScreen() {


    // get list of users and sort it by points
    val usersList = Final.finalDataList
    val sortedList = usersList.sortedWith(compareByDescending { it.points})

    Column(
        modifier = Modifier.fillMaxSize(),

    ){
        // display each user as a leaderboard item
        for (element in sortedList) {
            LeaderboardItem(user = element, rank = sortedList.indexOf(element) + 1)
        }
    }
}




