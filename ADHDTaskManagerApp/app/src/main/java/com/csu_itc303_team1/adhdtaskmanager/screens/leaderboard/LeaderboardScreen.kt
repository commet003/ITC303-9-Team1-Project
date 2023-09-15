package com.csu_itc303_team1.adhdtaskmanager.screens.leaderboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.csu_itc303_team1.adhdtaskmanager.common.composable.LeaderboardCard
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel = hiltViewModel()
) {
    val connectivityObserver = viewModel.connectivityObserver.collectAsState(initial = ConnectivityObserverService.Status.DISCONNECTED)

    when(connectivityObserver.value){
           ConnectivityObserverService.Status.CONNECTED -> {
               val leaderboardUsers = viewModel.leaderboardUsers.collectAsStateWithLifecycle(emptyList()).value.sortedByDescending { it.rewardPoints }
               val currentUserId = viewModel.getCurrentUserId()

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
                       Spacer(modifier = Modifier.height(10.dp))
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
                           items(leaderboardUsers.size){user ->
                               LeaderboardCard(leaderboardUsers[user], currentUserId, (user + 1))
                           }
                       }
                   }
               }
           }
        else -> {
            NoInternetScreen()
        }
    }
}


@Composable
fun NoInternetScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = "No Internet Connection")
        Icon(
            modifier = Modifier
                .padding(10.dp)
                .size(100.dp),
            imageVector = Icons.Filled.WifiOff,
            contentDescription = "No Internet Connection"
        )

    }
}