package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController


/**
 * This is Navigation Controller code. This page points to the screen that is requested.
 * Each new screen will need to be added here as well
 */

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    state: TodoState,
    event: (TodoEvent) -> Unit,
    rewardViewModel: RewardViewModel,

    ) {

}

