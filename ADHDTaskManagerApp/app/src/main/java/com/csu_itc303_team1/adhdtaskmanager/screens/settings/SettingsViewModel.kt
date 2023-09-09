package com.csu_itc303_team1.adhdtaskmanager.screens.settings

import com.csu_itc303_team1.adhdtaskmanager.LOGIN_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.SPLASH_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
) : MainViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(
            it.isAnonymous,
            isSignedIn()
            )
    }

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    // Is signed in
    fun isSignedIn() = accountService.hasUser

    fun onLinkAccountClick(openScreen: (String) -> Unit){
        launchCatching {
            accountService.linkAccount()
            /* Todo: Add a screen for linking accounts */
        }
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.deleteAccount()
            restartApp(SPLASH_SCREEN)
        }
    }
}