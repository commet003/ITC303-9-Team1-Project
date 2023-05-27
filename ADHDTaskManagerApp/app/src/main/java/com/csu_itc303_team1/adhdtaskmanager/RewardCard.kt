package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RewardCard(){

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
        elevation = 3.dp,
    ) {
        Row(
            modifier = Modifier.padding(15.dp)
        ){
            Column(){
                Text(
                    text = "Completed Task Badge",
                    fontSize = 24.sp
                )
                Text(
                    text = "You have achieved: " + "4" + " Times",
                    fontSize = 20.sp
                )
            }

            Column(){
                Text(
                    text = "Total Points",
                    fontSize = 20.sp
                )
                Text(
                    text = "12",
                    fontSize = 20.sp
                )
            }
        }
    }

}