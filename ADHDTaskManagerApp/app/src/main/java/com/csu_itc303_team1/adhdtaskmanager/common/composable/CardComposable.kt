package com.csu_itc303_team1.adhdtaskmanager.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.COMPLETED_TASK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.DEFAULT_PROFILE_PICTURE
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_REWARD
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_STREAK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_STREAK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.R
import com.csu_itc303_team1.adhdtaskmanager.common.ext.dropdownSelector
import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser

@ExperimentalMaterialApi
@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.error, modifier)
}

@ExperimentalMaterialApi
@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.onSurface, modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
private fun CardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier,
        onClick = onEditClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) { Text(stringResource(title), color = highlightColor) }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(painter = painterResource(icon), contentDescription = "Icon", tint = highlightColor)
        }
    }
}

@Composable
@ExperimentalMaterialApi
fun CardSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier,
    onNewValue: (String) -> Unit
) {

    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        DropdownSelector(label, options, selection, Modifier.dropdownSelector(), onNewValue = onNewValue)
    }
}


@Composable
fun RewardsCard(
    rewardEarned: Map.Entry<String, Int>
){
    var rewardTitle by remember {
        mutableStateOf("")
    }
    var rewardDescription by remember {
        mutableStateOf(0)
    }
    var pointsPerReward by remember {
        mutableStateOf(0)
    }

    when (rewardEarned.key){
        COMPLETED_TASK_REWARD_NAME -> {
            rewardTitle = "Completed Task Reward"
            rewardDescription = R.string.completed_task_reward_description
            pointsPerReward = COMPLETED_TASK_REWARD
        }
        LOGIN_REWARD_NAME -> {
            rewardTitle = "Login Reward"
            rewardDescription = R.string.login_reward_description
            pointsPerReward = LOGIN_REWARD
        }
        LOGIN_STREAK_REWARD_NAME -> {
            rewardTitle = "Login Streak Reward"
            rewardDescription = R.string.login_streak_reward_description
            pointsPerReward = LOGIN_STREAK_REWARD
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = rewardTitle,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Achieved:   ${rewardEarned.value}",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(80.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = stringResource(rewardDescription),
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total Points: ${rewardEarned.value * pointsPerReward}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                )
            }
        }
    }
}

@Composable
fun LeaderboardCard(
    user: FirestoreUser?,
    currentUserId: String,
    index: Int
) {
    ElevatedCard(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            colors =   when{
                (user?.id != currentUserId) -> CardDefaults.elevatedCardColors(
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
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = index.toString(),
                    modifier = Modifier.weight(0.8f),
                    color = when {
                        (user?.id != currentUserId) -> MaterialTheme.colorScheme.onSecondaryContainer
                        else -> MaterialTheme.colorScheme.onTertiaryContainer
                    },
                    fontSize = 18.sp
                )

                Image(
                    painter = rememberAsyncImagePainter(
                        model = if(user?.profilePictureUrl?.isNotEmpty()!!) user?.profilePictureUrl else DEFAULT_PROFILE_PICTURE),
                    contentDescription = "User's Profile Picture",
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(Shapes().extraLarge)
                )

                Spacer(modifier = Modifier.width(15.dp))


                Text(
                    text = user.username,
                    modifier = Modifier.weight(2f),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = user.rewardPoints.toString(),
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,  // Adjusted font size
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }
    }
