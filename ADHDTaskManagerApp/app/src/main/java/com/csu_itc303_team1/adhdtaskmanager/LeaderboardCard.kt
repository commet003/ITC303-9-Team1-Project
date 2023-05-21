package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// not used at the moment

@Composable
fun LeaderboardCard(
    user: Users
) {
    Card(
        shape = MaterialTheme.shapes.small,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ){
            user.displayName?.let { displayName ->
                Text(
                    text = displayName,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentWidth(Alignment.Start),
                    color = Color.DarkGray,
                    fontSize = 25.sp
                )
            }
            user.points?.let { points ->
                Text(
                    text = points.toString(),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End)
                    ,
                    color = Color.DarkGray,
                    fontSize = 25.sp
                )
            }
        }
    }
}