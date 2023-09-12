package com.csu_itc303_team1.adhdtaskmanager.screens.settings

import androidx.compose.runtime.mutableStateOf
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.SPLASH_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferences
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UserPreferencesService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logService: LogService,
    private val accountService: AccountService,
    private val userPreferencesService: UserPreferencesService
) : MainViewModel(logService) {
    val uiState = accountService.currentUser.map {
        SettingsUiState(
            it.isAnonymous,
            isSignedIn()
            )
    }



    private var initialSetup: UserPreferences? = null
    init {
        launchCatching {
             initialSetup = userPreferencesService.initialSetupEvent.value
        }
    }
    private val userPreferencesFlow = userPreferencesService.userPreferencesFlow
    val timerRounds = mutableStateOf("")

    fun onLoginClick(openScreen: (String) -> Unit) = openScreen(LOGIN_SCREEN)

    // Is signed in
    fun isSignedIn() = accountService.hasUser

    fun onLinkAccountClick(openScreen: (String) -> Unit){
        launchCatching {
            accountService.linkAccount()
            openScreen(LOGIN_SCREEN)
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

    // Get user preferences
    fun getUserPreferences() = userPreferencesFlow.map { it }

    // Update user preferences
    suspend fun updateDarkMode(darkMode: Boolean) {
        userPreferencesService.updateDarkThemeEnabled(darkMode)
    }

    suspend fun updateTimerRounds(timerRounds: Int) {
        userPreferencesService.updatePomodoroTimerRounds(timerRounds)
    }

    suspend fun updateFocusTimerDuration(focusTimerDuration: Long) {
        userPreferencesService.updatePomodoroTimerFocusLength(focusTimerDuration)
    }

    suspend fun updateShortBreakTimerDuration(shortBreakTimerDuration: Long) {
        userPreferencesService.updatePomodoroTimerShortBreakLength(shortBreakTimerDuration)
    }

    suspend fun updateLongBreakTimerDuration(longBreakTimerDuration: Long) {
        userPreferencesService.updatePomodoroTimerLongBreakLength(longBreakTimerDuration)
    }

    // get Dark Mode
    fun getPreferences(): Flow<UserPreferences> {
        return userPreferencesFlow
    }
}