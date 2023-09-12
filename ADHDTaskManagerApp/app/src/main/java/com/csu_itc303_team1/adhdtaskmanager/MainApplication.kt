package com.csu_itc303_team1.adhdtaskmanager

//noinspection UsingMaterialAndMaterial3Libraries
import android.Manifest
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.csu_itc303_team1.adhdtaskmanager.common.composable.BasicToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogCancelButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.DialogConfirmButton
import com.csu_itc303_team1.adhdtaskmanager.common.composable.NavToolbar
import com.csu_itc303_team1.adhdtaskmanager.common.composable.PermissionDialog
import com.csu_itc303_team1.adhdtaskmanager.common.composable.RationaleDialog
import com.csu_itc303_team1.adhdtaskmanager.common.snackbar.SnackbarManager
import com.csu_itc303_team1.adhdtaskmanager.screens.edit_task.EditTaskScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.help.HelpScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.login.LoginScreen
import com.csu_itc303_team1.adhdtaskmanager.screens.login.LoginViewModel
import com.csu_itc303_team1.adhdtaskmanager.screens.pomodoro_timer.Session
import com.csu_itc303_team1.adhdtaskmanager.screens.pomodoro_timer.Timer
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


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterialApi
fun MainApplication(
    viewModel: LoginViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    ADHDTaskManagerTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }
        var selectedItem by remember { mutableStateOf(TASKS_SCREEN) }
        var showWarningDialog by remember { mutableStateOf(false) }
        var isSignedIn by remember {
            mutableStateOf(false)
        }

        viewModel.authStateListener {
            isSignedIn = it
        }



        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()
            val coroutineScope = rememberCoroutineScope()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = remember { SnackbarHostState() },
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                },
                topBar = {
                    if (!isSignedIn)
                    {
                        BasicToolbar(title = R.string.app_name)
                    } else {
                        NavToolbar(
                            title = R.string.app_name,
                            navActionIcon = R.drawable.ic_nav_menu,
                            modifier = Modifier
                        ) {
                            coroutineScope.launch {
                                if (appState.getDrawerState().isClosed) appState.openDrawer()
                                else appState.closeDrawer()
                            }
                        }
                    }
                }
            ) { innerPaddingModifier ->
                ModalNavigationDrawer(
                    modifier = Modifier.padding(innerPaddingModifier),
                    drawerState = appState.getDrawerState(),
                    gesturesEnabled = false,
                    drawerContent = {
                        ModalDrawerSheet()
                        {
                            NavigationDrawerItem(
                                modifier = Modifier.padding(8.dp),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        tint = if(selectedItem == TASKS_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Home"
                                    )
                                },
                                label = { Text(text = "Home") },
                                colors = navigationItemColors(),
                                selected = selectedItem == TASKS_SCREEN,
                                onClick = {
                                    selectedItem = TASKS_SCREEN
                                    Log.d("Current Route", appState.currentRoute())
                                    appState.navigate(TASKS_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                modifier = Modifier.padding(8.dp),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        tint = if(selectedItem == REWARDS_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Rewards"
                                    )
                                },
                                label = { Text(text = "Rewards") },
                                colors = navigationItemColors(),
                                selected = selectedItem == REWARDS_SCREEN,
                                onClick = {
                                    selectedItem = REWARDS_SCREEN
                                    appState.navigate(REWARDS_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                modifier = Modifier.padding(8.dp),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.ListAlt,
                                        tint = if(selectedItem == LEADERBOARD_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Leaderboard"
                                    )
                                },
                                label = { Text(text = "Leaderboard") },
                                colors = navigationItemColors(),
                                selected = selectedItem == LEADERBOARD_SCREEN,
                                onClick = {
                                    selectedItem = LEADERBOARD_SCREEN
                                    appState.navigate(LEADERBOARD_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                }
                            )

                            NavigationDrawerItem(
                                modifier = Modifier.padding(8.dp),
                                label = { Text(text = "Pomodoro Timer")},
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Timer,
                                        tint = if(selectedItem == POMODORO_TIMER_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Home"
                                    )
                                },
                                colors = navigationItemColors(),
                                selected = selectedItem == POMODORO_TIMER_SCREEN,
                                onClick = {
                                    selectedItem = POMODORO_TIMER_SCREEN
                                    appState.navigate(POMODORO_TIMER_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                })

                            NavigationDrawerItem(
                                modifier = Modifier.padding(8.dp),
                                label = { Text(text = "Help") },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Help,
                                        tint = if(selectedItem == HELP_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Home"
                                    )
                                },
                                colors = navigationItemColors(),
                                selected = selectedItem == HELP_SCREEN,
                                onClick = {
                                    selectedItem = HELP_SCREEN
                                    appState.navigate(HELP_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                })

                            NavigationDrawerItem(
                                modifier = Modifier.padding(10.dp),
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        tint = if(selectedItem == SETTINGS_SCREEN) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                                        contentDescription = "Settings"
                                    )
                                },
                                colors = navigationItemColors(),
                                label = { Text(text = "Settings") },
                                selected = selectedItem == SETTINGS_SCREEN,
                                onClick = {
                                    selectedItem = SETTINGS_SCREEN
                                    appState.navigate(SETTINGS_SCREEN)
                                    coroutineScope.launch {
                                        appState.closeDrawer()
                                    }
                                }
                            )

                            if (isSignedIn){
                                NavigationDrawerItem(
                                    modifier = Modifier.padding(8.dp),
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Filled.ExitToApp,
                                            tint = MaterialTheme.colorScheme.primary,
                                            contentDescription = "Sign Out"
                                        )
                                    },
                                    label = { Text(text = "Sign Out") },
                                    colors = navigationItemColors(),
                                    selected = false,
                                    onClick = {
                                        showWarningDialog = true
                                        coroutineScope.launch {
                                            appState.closeDrawer()
                                        }
                                    }
                                )
                            }
                        }
                    },
                    content = {
                        if (showWarningDialog) {
                            showWarningDialog = SignOutDialog(
                                showDialog = showWarningDialog,
                                signOut = {
                                settingsViewModel.onSignOutClick { route ->
                                    appState.clearAndNavigate(route)
                                }
                            })
                        }
                            NavHost(
                                navController = appState.navController,
                                startDestination = SPLASH_SCREEN,
                                modifier = Modifier
                            ) {
                                navGraph(appState)
                            }
                    }
                )
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
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(navController, snackbarManager, resources, coroutineScope, drawerState) {
        AppState(drawerState, navController, snackbarManager, resources, coroutineScope)
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
            openScreen = { route -> appState.navigate(route) },
        )
    }

    composable(HELP_SCREEN) {
        HelpScreen()
    }


    composable(POMODORO_TIMER_SCREEN) {
        Column {
            Timer(
                duration = 25,
                onTimerCompleted = {Log.d("Timer", "Timer Completed")}
            )
            Session()
        }
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

@Composable
fun navigationItemColors(): NavigationDrawerItemColors {
    return NavigationDrawerItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.background,
        selectedTextColor = MaterialTheme.colorScheme.background,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedTextColor = MaterialTheme.colorScheme.primary,
        unselectedContainerColor = MaterialTheme.colorScheme.background,
    )
}

@ExperimentalMaterialApi
@Composable
private fun SignOutDialog(showDialog: Boolean, signOut: () -> Unit): Boolean {
        var showWarningDialog by remember { mutableStateOf(showDialog) }

        AlertDialog(
            title = { Text(stringResource(R.string.sign_out_title)) },
            text = { Text(stringResource(R.string.sign_out_description)) },
            dismissButton = { DialogCancelButton(R.string.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(R.string.sign_out) {
                    signOut()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    return showWarningDialog
}
