package com.csu_itc303_team1.adhdtaskmanager.ui.leaderboard_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.LeaderboardCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Final


@Composable
fun LeaderboardScreen() {

    // get list of users and sort it by points
    val usersList = Final.finalDataList.sortedWith(compareByDescending { it.rewardsPoints })
    Log.d("LeaderboardScreen", usersList.size.toString())

    /*Row(
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(
            text = "Rank",
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Name",
            modifier = Modifier.weight(2f),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Points",
            modifier = Modifier.weight(1f),
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(0.2f))
    }
    Spacer(modifier = Modifier.height(20.dp))*/

    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(0.dp, 5.dp, 0.dp, 0.dp)
    ) {
        items(usersList.size){user ->
            LeaderboardCard(user = usersList[user], rank = user + 1, userProfileImageUrl = usersList[user].profilePicture)
        }

    }
}


