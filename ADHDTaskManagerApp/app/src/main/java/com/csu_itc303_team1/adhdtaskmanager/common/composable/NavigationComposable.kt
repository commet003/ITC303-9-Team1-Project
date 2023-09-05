package com.csu_itc303_team1.adhdtaskmanager.common.composable

import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomNavigationDrawer(
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            CustomNavigationDrawerSheet()
        },
        content = {}
    )
}


@Composable
fun CustomNavigationDrawerSheet() {
    ModalDrawerSheet(
        drawerShape = MaterialTheme.shapes.medium,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        drawerTonalElevation = 4.dp,
        content = {/*TODO*/},
    )
}


@Composable
fun CustomNavigationDrawerItem(
    modifier: Modifier = Modifier,
) {
    NavigationDrawerItem(
        modifier = modifier,
        onClick = {},
        selected = false,
        icon = {},
        label = {}
    )
}