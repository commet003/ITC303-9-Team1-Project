package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

// Import CardElevation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData


val LeaderboardBlue = Color(0xFF045EA5)


@Composable
fun LeaderboardCard(user: UserData, rank: Int) {
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
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp)
        ) {
            Text(
                text = rank.toString(),
                modifier = Modifier.weight(1f),
                color = LeaderboardBlue,
                fontSize = 22.sp
            )
            Text(
                text = user.username ?: "",
                modifier = Modifier.weight(2f),
                color = Color.Black,
                fontSize = 22.sp
            )
            Text(
                text = user.loginStreak.toString(),
                modifier = Modifier.weight(2f),
                color = Color.Black,
                fontSize = 22.sp
            )
            Text(
                text = user.rewardsPoints.toString(),
                modifier = Modifier.weight(1f),
                color = LeaderboardBlue,
                fontSize = 22.sp,
                textAlign = TextAlign.End
            )
        }
    }
}
