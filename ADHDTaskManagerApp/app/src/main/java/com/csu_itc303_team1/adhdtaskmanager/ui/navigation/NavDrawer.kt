package com.csu_itc303_team1.adhdtaskmanager.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.csu_itc303_team1.adhdtaskmanager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val selectedItem = remember { mutableStateOf(Screen.SignInScreen) }
    val navController = rememberNavController()
    AppDrawer(
        scope = scope,
        navController = navController,
        selectedItem = selectedItem.value,
        drawerState = drawerState
    )
}

@Composable
private fun AppDrawer(
    scope: CoroutineScope,
    navController: NavController,
    selectedItem: Screen,
    drawerState: DrawerState,
    modifier: Modifier = Modifier
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            DrawerHeader()
            Screen.items.forEach { screen ->
                DrawerItem(
                    label = screen.title,
                    itemIcon = screen.icon,
                    screen = screen,
                    selectedItem = selectedItem,
                    action = {
                        selectedItem.route = screen.route
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(screen.route)
                    }
                )
            }
        }
    ) {

    }
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier,
) {
    NavigationDrawerItem(
        modifier= modifier
            .height(56.dp)
            .fillMaxWidth(),
        label = {
            Text(text = "ADHD Task Manager")
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "ADHD Task Manager"
            )
        },
        selected = false,
        onClick = {

        },
    )
}

@Composable
private fun DrawerItem(
    // painter: Painter,
    label: String,
    itemIcon: Int,
    screen: Screen,
    selectedItem: Screen,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawerItem(
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedContainerColor = MaterialTheme.colorScheme.background,
            unselectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.primary
        ),
        icon = {
            Icon(
                painter = painterResource(itemIcon),
                contentDescription = label,
            )
        },
        label = {
            Text(text = label)
        },
        selected = screen.route == selectedItem.route,
        onClick = action,
        modifier = modifier.padding(
            NavigationDrawerItemDefaults.ItemPadding
        )
    )
}


