package com.csu_itc303_team1.adhdtaskmanager

import android.content.Context
import android.graphics.fonts.FontStyle
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


// The Header of the Navigation Menu
@Composable
fun DrawerHeader(
    username: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primaryVariant)
            .padding(start = 10.dp, bottom = 20.dp),
        contentAlignment = Alignment.CenterStart
    ){


        Column{
            Text(modifier = Modifier.padding(top = 64.dp, bottom = 64.dp), text = "Header", fontSize = 60.sp, color = MaterialTheme.colors.onPrimary)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Hello, $username!", fontSize = 20.sp, color = MaterialTheme.colors.onPrimary)

        }
    }
}


// Drawer Item for each Screen the Menu will point to will be in the form of a DrawerItem
@Composable
fun DrawerItem(item: Screen, selected: Boolean, onItemClick: (Screen) -> Unit){


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(item) }
            .height(45.dp)
            .background(MaterialTheme.colors.primaryVariant)
            .padding(start = 10.dp)
    ) {
        // Menu Icon for the Drawer Item
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.title,
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(24.dp)
                .width(24.dp)
        )
        Spacer(modifier = Modifier.width(7.dp))
        // Text for the Drawer Item
        Text(
            text = item.title,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onPrimary
        )
    }
}

// This section is the main part of the Navigation Menu, the Drawer Body.
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun DrawerBody(context: Context, scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController, currentUser: AuthUiClient) {

    // Create a list of Screen objects
    val screens = listOf(
        Screen.TodoScreen,
        Screen.CompletedScreen,
        Screen.RewardsScreen,
        Screen.LeaderboardScreen,
        Screen.SettingsScreen,
        Screen.HelpScreen
    )

    // Column to store all the items
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = MaterialTheme.colors.primaryVariant)
            .verticalScroll(rememberScrollState()),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // For Each Screen, it creates a Drawer Item
        screens.forEach { items ->
            DrawerItem(item = items, selected = currentRoute == items.route, onItemClick = {

                // When an item is clicked, it navigates to the route of the screen object
                // and displays the screen on the Main Activity Screen. It then remembers which
                // screen it is on
                navController.navigate(items.route) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }

                scope.launch {
                    scaffoldState.drawerState.close()
                }
            })
        }

        if (currentUser.getSignedInUser() != null) {
            // Sign out suspend clickable text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            currentUser.signOut()
                            Toast
                                .makeText(
                                    context,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                )
                                .show()
                            navController.popBackStack()
                        }
                    }
                    .height(45.dp)
                    .padding(start = 10.dp)
            ) {


                Row(modifier = Modifier.fillMaxWidth()){
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Sign Out",
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.primaryVariant)
                            .height(24.dp)
                            .width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    // Text for the Drawer Item
                    Text(
                        text = "Sign Out",
                        fontSize = 16.sp,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }

            if (currentUser.isUserAnonymous()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "You are signed in anonymously, if you sign out you will lose all your data.",
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                        color = Color(0xFFFC8B8B),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                            .height(30.dp)
                    )
                }
            }
        }
    }
}