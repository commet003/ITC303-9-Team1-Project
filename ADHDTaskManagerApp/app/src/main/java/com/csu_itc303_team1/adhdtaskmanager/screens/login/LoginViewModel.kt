package com.csu_itc303_team1.adhdtaskmanager.screens.login

import androidx.compose.runtime.mutableStateOf
import com.csu_itc303_team1.adhdtaskmanager.SETTINGS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASKS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : MainViewModel(logService) {
    var uiState = mutableStateOf(LoginUiState())
        private set

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {

        launchCatching {
            accountService.authenticateWithGoogle()
            openAndPopUp(TASKS_SCREEN, SETTINGS_SCREEN)
        }
    }

    fun onSignInAnonymouslyClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.createAnonymousAccount()
            openAndPopUp(TASKS_SCREEN, SETTINGS_SCREEN)
        }
    }
}