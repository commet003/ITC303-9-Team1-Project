package com.csu_itc303_team1.adhdtaskmanager.ui.leaderboard_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.LeaderboardCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Final

@Composable
fun LeaderboardScreen() {


    // get list of users and sort it by points
    val usersList = Final.finalDataList
    val sortedList = usersList.sortedWith(compareByDescending { it.rewardsPoints})

    Row(modifier = Modifier.padding(12.dp)) {
        Text(
            text = "Rank",
            modifier = Modifier.weight(1f),
            color = LeaderboardBlue,
            fontSize = 18.sp
        )
        Text(
            text = "Name",
            modifier = Modifier.weight(2f),
            color = LeaderboardBlue,
            fontSize = 18.sp
        )
        Text(
            text = "Country",
            modifier = Modifier.weight(2f),
            color = LeaderboardBlue,
            fontSize = 18.sp
        )
        Text(
            text = "Points",
            modifier = Modifier.weight(1f),
            color = LeaderboardBlue,
            fontSize = 18.sp
        )
    }
    Spacer(modifier = Modifier.height(20.dp))

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(0.dp, 35.dp, 0.dp)
    ) {
        items(items = sortedList) { element ->
            LeaderboardCard(user = element, rank = sortedList.indexOf(element) + 1)
        }
    }
}


