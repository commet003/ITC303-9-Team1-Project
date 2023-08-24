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
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.Reward

@Composable
fun RewardCard(reward: Reward){
    val total: Int = reward.timesAchieved * reward.pointsAwarded


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
                    text = reward.title,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                    if(reward.timesAchieved > 1){
                        Text(
                        text = "You have achieved this reward " + reward.timesAchieved + " Times",
                        fontSize = 20.sp
                        )
                    }
                    else {
                        Text(
                            text = "You have achieved this reward " + reward.timesAchieved + " Time",
                            fontSize = 20.sp
                        )

                    }



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
                if(total > 1){
                Text(
                    text = " You have earned $total points",
                    fontSize = 20.sp
                )

            }
                else{
                    Text(
                        text = " You have earned $total point",
                        fontSize = 20.sp
                    )

                }

            }

        }

    }

}