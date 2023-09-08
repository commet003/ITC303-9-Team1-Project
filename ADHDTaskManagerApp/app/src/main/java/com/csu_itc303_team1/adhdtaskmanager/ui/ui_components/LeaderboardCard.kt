package com.csu_itc303_team1.adhdtaskmanager.ui.ui_components

// Import CardElevation
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.csu_itc303_team1.adhdtaskmanager.DEFAULT_PROFILE_PICTURE
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData


@OptIn(ExperimentalCoilApi::class)
@Composable
fun LeaderboardCard(user: UserData, rank: Int, currentUserId: String) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 5.dp
            )
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        colors = when{
            (user.userID != currentUserId) -> CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            else -> CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }


    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            verticalAlignment = Alignment.CenterVertically,  // Vertically centering the content
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.weight(0.2f))
            Text(
                text = rank.toString(),
                modifier = Modifier.weight(0.8f),
                color = when {
                    (user.userID != currentUserId) -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onTertiaryContainer
                },
                fontSize = 18.sp  // Adjusted font size
            )

            Image(
                painter = rememberImagePainter(
                    data = if(user.profilePicture != "null") user.profilePicture else DEFAULT_PROFILE_PICTURE,
                ),
                contentDescription = "User's Profile Picture",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(Shapes().extraLarge)
            )

            Spacer(modifier = Modifier.weight(0.2f))


            Text(
                text = if (user.username != "") user.username!! else user.userID?.slice(0..5)!!,
                modifier = Modifier.weight(2f),
                color = when {
                    (user.userID != currentUserId) -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onTertiaryContainer
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = user.rewardsPoints.toString(),
                modifier = Modifier.weight(1f),
                color = when {
                    (user.userID != currentUserId) -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.secondary
                },
                fontSize = 18.sp,  // Adjusted font size
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

