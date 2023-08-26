package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.REWARDS
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData

@Composable
fun RewardCard(
    rewardTitle: String,
    index: Int,
    currentUser: UserData){
    val rewardTimesAchieved = if(currentUser.rewardsEarned?.keys?.elementAt(index) != null) currentUser.rewardsEarned[rewardTitle] else 0
    val total = rewardTimesAchieved?.times(REWARDS.values.elementAt(index))

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
            .fillMaxWidth(),
        //elevation = 3.dp,


    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ){
            Column {
                Text(
                    text = when (rewardTitle) {
                        "SIGN_IN_REWARD" -> "Sign In Reward"
                        "TASK_COMPLETED_REWARD" -> "Task Completed Reward"
                        "WEEK_LONG_STREAK_REWARD" -> "Week Long Streak Reward"
                        else -> { "Reward"}
                    },
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                        Text(
                        text = if (rewardTimesAchieved != null && rewardTimesAchieved > 1) "You have achieved this reward $rewardTimesAchieved Times" else "You have achieved this reward $rewardTimesAchieved Time",
                        fontSize = 20.sp
                        )




            }
            //Spacer(modifier = Modifier.height(16.dp))

//            Column {
//                Text(
//                    text = "Total Points",
//                    fontSize = 20.sp
//                )
//                Text(
//                    text = total.toString(),
//                    fontSize = 20.sp
//                )
//            }
        }

        Row(
            modifier = Modifier.padding(10.dp)
        ){
            Column {
                Text(
                    text = if (total != null && total > 1) " You have earned $total points" else " You have earned $total point",
                    fontSize = 20.sp
                )

            }

        }

    }

}