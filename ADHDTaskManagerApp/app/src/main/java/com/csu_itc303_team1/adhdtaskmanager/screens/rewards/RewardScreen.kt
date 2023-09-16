package com.csu_itc303_team1.adhdtaskmanager.screens.rewards

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_STREAK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_STREAK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RewardsCard
import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService
import com.csu_itc303_team1.adhdtaskmanager.screens.leaderboard.NoInternetScreen

@SuppressLint("FlowOperatorInvokedInComposition", "CoroutineCreationDuringComposition")
@Composable
fun RewardsScreen(
    rewardViewModel: RewardViewModel = hiltViewModel(),
) {
    val connectivityObserver = rewardViewModel.connectivityObserver.collectAsState(initial = ConnectivityObserverService.Status.DISCONNECTED)

    when(connectivityObserver.value){
        ConnectivityObserverService.Status.CONNECTED -> {
            val user = rewardViewModel.getUser().collectAsState(initial = FirestoreUser()).value
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp, bottom = 10.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = if(user?.username != null) user.username else "Username Not Found",
                        fontSize = 30.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Points",
                        fontSize = 26.sp,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (user?.rewardPoints != null) user.rewardPoints.toString() else "0",
                        fontSize = 26.sp,
                    )
                }

                RewardsCard(
                    rewardTitle = "Completed Task",
                    rewardDescription = R.string.completed_task_reward_description,
                    rewardCount = user?.rewardsEarned?.get(COMPLETED_TASK_REWARD_NAME) ?: 0,
                    pointsPerReward = COMPLETED_TASK_REWARD,
                    rewardPoints = user?.rewardPoints ?: 0,


                )

                RewardsCard(
                    rewardTitle = "Login Reward",
                    rewardDescription = R.string.login_reward_description,
                    rewardCount = user?.rewardsEarned?.get(LOGIN_REWARD_NAME) ?: 0,
                    pointsPerReward = 0,
                    rewardPoints = user?.rewardPoints ?: 0,


                )

                RewardsCard(
                    rewardTitle = "Login Streak",
                    rewardDescription = R.string.login_streak_reward_description,
                    rewardCount = user?.rewardsEarned?.get(LOGIN_STREAK_REWARD_NAME) ?: 0,
                    pointsPerReward = LOGIN_STREAK_REWARD,
                    rewardPoints = user?.rewardPoints ?: 0

                )
            }
        }
        else -> {
            NoInternetScreen()
        }
    }
}