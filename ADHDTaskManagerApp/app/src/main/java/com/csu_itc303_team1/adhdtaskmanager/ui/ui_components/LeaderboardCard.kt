package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

// Import CardElevation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Users

@Composable
fun LeaderboardCard(
    user: Users,
    rank: Int
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
       /* elevation = CardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 16.dp,
            focusedElevation = 8.dp,
            hoveredElevation = 8.dp,
            draggedElevation = 8.dp,
            disabledElevation = 0.dp
        ),*/
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ){
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = rank.toString(),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start),
                    color = Color.DarkGray,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(50.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.displayName.toString(),
                    modifier = Modifier,
                    color = Color.DarkGray,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.width(45.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = user.country.toString(),
                    modifier = Modifier,
                    color = Color.DarkGray,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.width(60.dp))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = user.points.toString(),
                    modifier = Modifier
                        .wrapContentWidth(Alignment.End),
                    color = Color.DarkGray,
                    fontSize = 22.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}