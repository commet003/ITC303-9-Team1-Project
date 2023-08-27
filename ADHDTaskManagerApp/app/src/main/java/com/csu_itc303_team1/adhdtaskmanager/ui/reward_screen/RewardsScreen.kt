package com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
<<<<<<< HEAD
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
=======
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.userRewardViewModel.UserRewardViewModel
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)

@Composable
<<<<<<< HEAD
fun RewardsScreen(
    currentUser: String,
    firestoreViewModel: FirestoreViewModel
) {
    val user = firestoreViewModel.getUser(currentUser)
=======
fun RewardsScreen(userRewardViewModel: UserRewardViewModel) {

    val allRewards by userRewardViewModel.allRewards.observeAsState(listOf())
    val currentUser = userRewardViewModel.user.collectAsState()
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 20.dp, 20.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                if (currentUser.value?.userID == null) {
                    Text(
                        text = "No Id set",
                        fontSize = 30.sp
                    )
                } else {
                    currentUser.value!!.username?.let {
                        Text(
                            text = it,
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
                    text = currentUser.value?.points.toString(),
                    fontSize = 26.sp,
                    modifier = Modifier.absoluteOffset(100.dp, 100.dp)
                )
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(10.dp, 150.dp, 10.dp)
    ) {
<<<<<<< HEAD
        items(REWARDS_COUNTS.size) { reward ->
            RewardCard(user?.rewardsEarned?.keys?.elementAt(reward)!!, reward, user)
=======
        items(allRewards) { reward ->
            RewardCard(reward)
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)
        }
    }
}