package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

@Composable
fun RewardSetup(viewModel: RewardViewModel) {

    val allRewards by viewModel.allRewards.observeAsState(listOf())
    val searchResults by viewModel.searchResults.observeAsState(listOf())

    RewardsScreen(allRewards)
    returnRewardViewModel(viewModel)
}

fun returnRewardViewModel(rewardViewModel: RewardViewModel):RewardViewModel{
    return rewardViewModel
}
