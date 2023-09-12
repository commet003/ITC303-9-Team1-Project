package com.csu_itc303_team1.adhdtaskmanager.screens.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.SPLASH_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASKS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConfigurationService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    configurationService: ConfigurationService,
    private val accountService: AccountService,
    logService: LogService
) : MainViewModel(logService) {
    val showError = mutableStateOf(false)
    var currentUser = accountService.currentUser

    init {
        launchCatching {
            configurationService.fetchConfiguration()
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {

        showError.value = false
        if (accountService.hasUser){
            openAndPopUp(TASKS_SCREEN, SPLASH_SCREEN)
            Log.d("SplashViewModel", "User ${accountService.currentUserId} logged in")
        } else {
            openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
        }
    }
}