package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.liveData
import com.csu_itc303_team1.adhdtaskmanager.data.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferencesRepository
import com.csu_itc303_team1.adhdtaskmanager.model.service.UserPreferencesService
import javax.inject.Inject

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME,
    produceMigrations = { context ->
        // Since we're migrating from SharedPreferences, add a migration based on the
        // SharedPreferences name
        listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME))
    }
)


class UserPreferencesServiceImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
): UserPreferencesService {
    override val initialSetupEvent = liveData {
        emit(userPreferencesRepository.fetchInitialPreferences())
    }

    override val userPreferencesFlow = userPreferencesRepository.userPreferencesFlow
    override suspend fun updateShowCompleted(showCompleted: Boolean) {
        userPreferencesRepository.setShowCompleted(showCompleted)
    }

    override suspend fun updateShowUncompleted(showUncompleted: Boolean) {
        userPreferencesRepository.setShowUncompleted(showUncompleted)
    }

    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        userPreferencesRepository.setSortOrder(sortOrder)
    }

    override suspend fun updatePomodoroTimerRounds(pomodoroTimerRounds: Int) {
        userPreferencesRepository.setPomodoroTimerRounds(pomodoroTimerRounds)
    }

    override suspend fun updatePomodoroTimerFocusLength(pomodoroTimerFocusLength: Long) {
        userPreferencesRepository.setPomodoroTimerFocusDuration(pomodoroTimerFocusLength)
    }

    override suspend fun updatePomodoroTimerShortBreakLength(pomodoroTimerShortBreakLength: Long) {
        userPreferencesRepository.setPomodoroTimerShortBreakDuration(pomodoroTimerShortBreakLength)
    }

    override suspend fun updatePomodoroTimerLongBreakLength(pomodoroTimerLongBreakLength: Long) {
        userPreferencesRepository.setPomodoroTimerLongBreakDuration(pomodoroTimerLongBreakLength)
    }

    override suspend fun updateDarkThemeEnabled(darkThemeEnabled: Boolean) {
        userPreferencesRepository.setDarkMode(darkThemeEnabled)
    }

    override suspend fun getDarkThemeEnabled(): Boolean {
        return userPreferencesRepository.getDarkMode()
    }
    override suspend fun getShowCompleted(): Boolean {
        return userPreferencesRepository.getShowCompleted()
    }

    override suspend fun getShowUncompleted(): Boolean {
        return userPreferencesRepository.getShowUncompleted()
    }

    override suspend fun getSortOrder(): SortOrder {
        return userPreferencesRepository.getSortOrder()
    }

    override suspend fun getPomodoroTimerRounds(): Int {
        return userPreferencesRepository.getPomodoroTimerRounds()
    }

    override suspend fun getPomodoroTimerFocusLength(): Long {
        return userPreferencesRepository.getPomodoroTimerFocusDuration()
    }

    override suspend fun getPomodoroTimerShortBreakLength(): Long {
        return userPreferencesRepository.getPomodoroTimerShortBreakDuration()
    }

    override suspend fun getPomodoroTimerLongBreakLength(): Long {
        return userPreferencesRepository.getPomodoroTimerLongBreakDuration()
    }
}