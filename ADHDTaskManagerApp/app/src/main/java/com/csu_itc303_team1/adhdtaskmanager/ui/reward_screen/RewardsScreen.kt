package com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.RewardCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Users
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RewardsScreen(rewardViewModel: RewardViewModel, usersViewModel: UsersViewModel) {

    val allRewards by rewardViewModel.allRewards.observeAsState(listOf())

    //Firebase Authentication, get the userId of the currently logged-in user.
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Fetching the user data if userId is not null.
    if (userId != null) {
        usersViewModel.getUser(userId)
    }


    val user = usersViewModel.user.collectAsState(initial = Users()).value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = user?.username ?: "Username Not Found",
                    fontSize = 30.sp
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp, 20.dp)
    ) {
        Text(
            text = "Total Points",
            fontSize = 26.sp,
            modifier = Modifier.absoluteOffset(50.dp, 100.dp)
        )
        Text(
            text = user?.points?.toString() ?: "0", // Changed this to use the user's points.
            fontSize = 26.sp,
            modifier = Modifier.absoluteOffset(100.dp, 100.dp)
        )
    }

    LazyColumn(
        modifier = Modifier.padding(10.dp, 150.dp, 10.dp)
    ) {
        items(allRewards) { reward ->
            RewardCard(reward)
        }
    }
}
