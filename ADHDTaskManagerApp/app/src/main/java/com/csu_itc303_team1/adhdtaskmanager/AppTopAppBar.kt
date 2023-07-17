package com.csu_itc303_team1.adhdtaskmanager

import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppTopAppBar(scope: CoroutineScope, scaffoldState: ScaffoldState, currentUser: AuthUiClient){

    // This is for the Bar at the top of the application. This will be present on every page.
    TopAppBar(
        title = { Text(text = "ADHD Task Manager", fontSize = 18.sp) }, // Label on the bar
        // If the user is signed in, show the navigation icon
        navigationIcon = if(currentUser.getSignedInUser() != null) {
            {
                IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        } else null
        ,
        backgroundColor = MaterialTheme.colors.primary,         // Colours of the bar
        contentColor = MaterialTheme.colors.onPrimary
    )
}
