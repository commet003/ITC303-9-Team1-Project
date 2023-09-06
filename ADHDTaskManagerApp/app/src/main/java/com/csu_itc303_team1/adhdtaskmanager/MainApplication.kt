package com.csu_itc303_team1.adhdtaskmanager

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csu_itc303_team1.adhdtaskmanager.common.composable.PermissionDialog
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RationaleDialog
import com.csu_itc303_team1.adhdtaskmanager.common.snackbar.SnackbarManager
import com.csu_itc303_team1.adhdtaskmanager.screens.edit_task.EditTaskScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.login.LoginScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.settings.SettingsScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.settings.SettingsViewModel
import com.csu_itc303_team1.adhdtaskmanager.screens.splash.SplashScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.tasks.TasksScreen
import com.csu_itc303_team1.adhdtaskmanager.theme.ADHDTaskManagerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.csu_itc303_team1.adhdtaskmanager.R.drawable as AppIcon
import com.csu_itc303_team1.adhdtaskmanager.R.string as AppText


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterialApi
fun MainApplication() {
    ADHDTaskManagerTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()
            val scope = rememberCoroutineScope()
            val settingsViewModel = hiltViewModel<SettingsViewModel>()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = remember { SnackbarHostState() },
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState,
                drawerContent = {
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                NavigationDrawerItem(
                                    label = { AppText.tasks },
                                    icon = { AppIcon.ic_tasks },
                                    selected = appState.navController.currentDestination?.route == TASKS_SCREEN,
                                    onClick = {
                                        appState.navigate(TASKS_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                NavigationDrawerItem(
                                    label = { AppText.rewards },
                                    icon = { AppIcon.ic_rewards },
                                    selected = appState.navController.currentDestination?.route == REWARDS_SCREEN,
                                    onClick = {
                                        appState.navigate(REWARDS_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                NavigationDrawerItem(
                                    label = { AppText.leaderboard },
                                    selected = appState.navController.currentDestination?.route == LEADERBOARD_SCREEN,
                                    onClick = {
                                        appState.navigate(LEADERBOARD_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                NavigationDrawerItem(
                                    label = { AppText.pomodoro_timer },
                                    icon = { AppIcon.ic_timer },
                                    selected = appState.navController.currentDestination?.route == POMODORO_TIMER_SCREEN,
                                    onClick = {
                                        appState.navigate(POMODORO_TIMER_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                NavigationDrawerItem(
                                    label = { AppText.help },
                                    icon = { AppIcon.ic_help },
                                    selected = appState.navController.currentDestination?.route == HELP_SCREEN,
                                    onClick = {
                                        appState.navigate(HELP_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                NavigationDrawerItem(
                                    label = { AppText.settings },
                                    icon = { AppIcon.ic_settings },
                                    selected = appState.navController.currentDestination?.route == SETTINGS_SCREEN,
                                    onClick = {
                                        appState.navigate(SETTINGS_SCREEN)
                                        scope.launch {
                                            appState.toggleDrawerState()
                                        }
                                    }
                                )

                                when(settingsViewModel.isSignedIn()){
                                    true -> {
                                        NavigationDrawerItem(
                                            label = { AppText.sign_out },
                                            icon = { AppIcon.ic_exit },
                                            selected = false,
                                            onClick = {
                                                settingsViewModel.onSignOutClick { route ->
                                                    appState.clearAndNavigate(route)
                                                }
                                                scope.launch {
                                                    appState.toggleDrawerState()
                                                }
                                            }
                                        )
                                    }
                                    false -> {
                                        NavigationDrawerItem(
                                            label = { AppText.sign_in },
                                            icon = { AppIcon.ic_sign_in },
                                            selected = false,
                                            onClick = {
                                                settingsViewModel.onLoginClick { route ->
                                                    appState.clearAndNavigate(route)
                                                }
                                                scope.launch {
                                                    appState.toggleDrawerState()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    ) {

                    }
                },
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SPLASH_SCREEN,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    navGraph(appState)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAppState(
    drawerState: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange = { true }
    ),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackbarManager, resources, coroutineScope, drawerState) {
        AppState(drawerState, scaffoldState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
fun NavGraphBuilder.navGraph(appState: AppState) {
    composable(SPLASH_SCREEN) {
        SplashScreen(
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(TASKS_SCREEN) {
        TasksScreen(
            openScreen = { route -> appState.navigate(route) })
    }

    composable(
        route = "$EDIT_TASK_SCREEN$TASK_ID_ARG",
        arguments = listOf(navArgument(TASK_ID) {
            nullable = true
            defaultValue = null
        })
    ) {
        EditTaskScreen(
            popUpScreen = { appState.popUp() }
        )
    }
}
