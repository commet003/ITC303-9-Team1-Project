package com.csu_itc303_team1.adhdtaskmanager.model.service

import androidx.lifecycle.LiveData
import com.csu_itc303_team1.adhdtaskmanager.data.SortOrder
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesService{
    val initialSetupEvent: LiveData<UserPreferences>
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateShowCompleted(showCompleted: Boolean)
    suspend fun updateShowUncompleted(showUncompleted: Boolean)
    suspend fun updateSortOrder(sortOrder: SortOrder)
    suspend fun updatePomodoroTimerRounds(pomodoroTimerRounds: Int)
    suspend fun updatePomodoroTimerFocusLength(pomodoroTimerFocusLength: Long)
    suspend fun updatePomodoroTimerShortBreakLength(pomodoroTimerShortBreakLength: Long)
    suspend fun updatePomodoroTimerLongBreakLength(pomodoroTimerLongBreakLength: Long)

    suspend fun updateUserPoints(userPoints: Int)

    suspend fun updateRewardsEarned(rewardsEarned: MutableMap<String, Int>)


    suspend fun updateDarkThemeEnabled(darkThemeEnabled: Boolean)

    suspend fun getRewardsEarned(): MutableMap<String, Int>

    suspend fun getUserPoints(): Int

    suspend fun getDarkThemeEnabled(): Boolean

    suspend fun getShowCompleted(): Boolean
    suspend fun getShowUncompleted(): Boolean
    suspend fun getSortOrder(): SortOrder
    suspend fun getPomodoroTimerRounds(): Int

    suspend fun getPomodoroTimerFocusLength(): Long
    suspend fun getPomodoroTimerShortBreakLength(): Long
    suspend fun getPomodoroTimerLongBreakLength(): Long


}