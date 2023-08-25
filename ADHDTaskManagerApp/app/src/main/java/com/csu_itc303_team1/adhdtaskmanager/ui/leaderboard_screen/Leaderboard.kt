package com.csu_itc303_team1.adhdtaskmanager.ui.leaderboard_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData


val LeaderboardBlue = Color(0xFF045EA5)

@Composable
fun LeaderboardItem(user: UserData, rank: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 10.dp)
    ) {
        Text(
            text = rank.toString(),
            fontSize = 22.sp,
            color = LeaderboardBlue
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = user.username ?: "",
            fontSize = 22.sp,
            color = LeaderboardBlue
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = user.loginStreak.toString(),
            fontSize = 22.sp,
            color = LeaderboardBlue
        )
        Spacer(modifier = Modifier.width(50.dp))
        Text(
            text = user.rewardsPoints.toString(),
            fontSize = 22.sp,
            color = LeaderboardBlue
        )
    }
}

