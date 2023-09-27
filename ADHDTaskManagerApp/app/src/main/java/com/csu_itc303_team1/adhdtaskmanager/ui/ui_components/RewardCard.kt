package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.Reward

@Composable
fun RewardCard(reward: Reward, title: String) {
    val total: Int = reward.timesAchieved * reward.pointsAwarded

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth()
            .background(Color.White),
        elevation = 4.dp // Sets the elevation here
    ) {
        Column {  // Wrap the Rows in a Column
            Row(
                modifier = Modifier.padding(15.dp)
            ) {
                Column {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (reward.timesAchieved > 1)
                            "You have achieved this reward ${reward.timesAchieved} Times"
                        else
                            "You have achieved this reward ${reward.timesAchieved} Time",
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier.padding(10.dp)
            ) {
                Column {
                    Text(
                        text = if (total > 1)
                            "You have earned $total points"
                        else
                            "You have earned $total point",
                        fontSize = 20.sp,
                        color = LeaderboardBlue
                    )
                }
            }
        }
    }
}