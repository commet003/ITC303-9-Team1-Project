package com.csu_itc303_team1.adhdtaskmanager

import android.content.res.Resources
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.csu_itc303_team1.adhdtaskmanager.common.snackbar.SnackbarManager
import com.csu_itc303_team1.adhdtaskmanager.common.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class AppState(
    private val drawerState: DrawerState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
            }
        }
    }

    suspend fun openDrawer() {
        drawerState.open()
    }

    suspend fun closeDrawer() {
        drawerState.close()
    }

    fun getDrawerState(): DrawerState {
        return drawerState
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

