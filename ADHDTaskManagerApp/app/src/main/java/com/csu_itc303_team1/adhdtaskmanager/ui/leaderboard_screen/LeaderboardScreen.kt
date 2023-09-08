package com.csu_itc303_team1.adhdtaskmanager.ui.leaderboard_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.LeaderboardCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Final
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LeaderboardScreen(
    firestoreViewModel: FirestoreViewModel
) {

    // get list of users and sort it by points
    val usersList = Final.finalDataList.sortedWith(compareByDescending { it.rewardsPoints })
    val currentUserId = firestoreViewModel.user.value?.userID

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.2f))
                Text(
                    text = "Rank",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Name",
                    modifier = Modifier.weight(2f),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Points",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(0.2f))
            }
            LazyColumn(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
            ) {
                items(usersList.size){user ->
                    LeaderboardCard(user = usersList[user], rank = user + 1, currentUserId!!)
                }
            }
        }
    }
}