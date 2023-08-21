package com.csu_itc303_team1.adhdtaskmanager.ui.leaderboard_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.ui.ui_components.LeaderboardCard
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Final

@Composable
fun LeaderboardScreen() {


    // get list of users and sort it by points
    val usersList = Final.finalDataList
    val sortedList = usersList.sortedWith(compareByDescending { it.points})

//    Column(
//        modifier = Modifier.fillMaxSize(),
//
//    ){
//        // display each user as a leaderboard item
//        for (element in sortedList) {
//            LeaderboardItem(user = element, rank = sortedList.indexOf(element) + 1)
//        }
//    }
    Row {
        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Rank",
            modifier = Modifier
                .wrapContentWidth(Alignment.Start),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(60.dp))

        Text(
            text = "Name",
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(60.dp))

        Text(
            text = "Country",
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(60.dp))

        Text(
            text = "Points",
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
            ,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
    }
    Spacer(modifier = Modifier.height(7.dp))

    LazyColumn(
        modifier = Modifier,
        contentPadding = PaddingValues(0.dp, 20.dp, 0.dp)
    ) {
        items(items = sortedList) { element ->
            LeaderboardCard(user = element, rank = sortedList.indexOf(element) + 1)
        }
    }


}
