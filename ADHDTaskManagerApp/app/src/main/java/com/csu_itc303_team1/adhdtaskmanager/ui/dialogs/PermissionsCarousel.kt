package com.csu_itc303_team1.adhdtaskmanager.ui.dialogs

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun PermissionsCarousel(
    modifier: Modifier = Modifier
) {


    val requiredPermissions = listOf(
        "POST_NOTIFICATION",
        "SETTING_WRITE",
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        // provide pageCount
        requiredPermissions.size
    }
    val scope = rememberCoroutineScope()
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { /*TODO*/ }) {
        Column(modifier = modifier
            .padding(top = 8.dp)
            .width(400.dp)
            .height(450.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalPager(
                state = pagerState,
                key = { requiredPermissions[it] },
                pageSize = PageSize.Fill,
                pageContent = { index ->
                    Column(
                        modifier = modifier
                            .clip(RoundedCornerShape(16.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        PermissionsCarouselItem(requiredPermissions[index])
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionsCarouselItem(
    permission: String,
    modifier: Modifier = Modifier
) {
    Column(
        //horizontalAlignment = Alignment.CenterHorizontally,
        //verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .width(350.dp)
            .height(400.dp)
    ) {
        Column(
            modifier = modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            val permissionTitle = when (permission) {
                "POST_NOTIFICATION" -> "Post Notifications"
                "SETTING_WRITE" -> "Write Settings"
                else -> "Unknown"
            }

            Text(text = permissionTitle, style = MaterialTheme.typography.titleLarge)
            Text(text = "This permission is required for the app to function properly.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


