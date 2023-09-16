package com.csu_itc303_team1.adhdtaskmanager.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


enum class SortOrder {
    NONE,
    BY_DEADLINE,
    BY_PRIORITY,
    BY_DEADLINE_AND_PRIORITY,
    BY_CATEGORY,
    BY_DEADLINE_AND_CATEGORY
}

data class UserPreferences(
    val showCompleted: Boolean = false,
    val showUncompleted: Boolean = true,
    val sortOrder: SortOrder = SortOrder.NONE,
    var darkMode: Boolean = false,
    var pomodoroTimerRounds: Int = 4,
    val pomodoroTimerFocusDuration: Long = 25L,
    val pomodoroTimerShortBreakDuration: Long = 5L,
    val pomodoroTimerLongBreakDuration: Long = 15L,
    var userRewardsPoints: Int = 0,
    val userRewardsEarnedCompletedTask: Int = 0,
    val userRewardsEarnedLogin: Int = 0,
    val userRewardsEarnedLoginStreak: Int = 0,
)

// domain layer
interface UserPrefRepository {

    suspend fun setShowCompleted(
        showCompleted: Boolean
    )

    suspend fun setShowUncompleted(
        showUncompleted: Boolean
    )

    suspend fun setPomodoroTimerRounds(
        pomodoroTimerRounds: Int
    )

    suspend fun setPomodoroTimerFocusDuration(
        pomodoroTimerFocusDuration: Long
    )

    suspend fun setPomodoroTimerShortBreakDuration(
        pomodoroTimerShortBreakDuration: Long
    )

    suspend fun setPomodoroTimerLongBreakDuration(
        pomodoroTimerLongBreakDuration: Long
    )

    suspend fun setSortOrder(
        sortOrder: SortOrder
    )

    suspend fun setDarkMode(
        darkMode: Boolean
    )

    suspend fun updateAllPreferences(
        showCompleted: Boolean,
        sortOrder: SortOrder,
        darkMode: Boolean
    )

    suspend fun setUserRewardsPoints(
        userRewardsPoints: Int
    )

    suspend fun setUserRewardsEarned(
        userRewardsEarned: MutableMap<String, Int>
    )

    suspend fun getUserRewardsPoints(): Int

    suspend fun getUserRewardsEarned(): MutableMap<String, Int>

    suspend fun getAllPreferences(): UserPreferences

    suspend fun getShowCompleted(): Boolean

    suspend fun getShowUncompleted(): Boolean

    suspend fun getPomodoroTimerRounds(): Int

    suspend fun getPomodoroTimerFocusDuration(): Long

    suspend fun getPomodoroTimerShortBreakDuration(): Long

    suspend fun getPomodoroTimerLongBreakDuration(): Long

    suspend fun getSortOrder(): SortOrder

    suspend fun getDarkMode(): Boolean
}

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefRepository {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            mapUserPreferences(preferences)
        }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
        val SHOW_UNCOMPLETED = booleanPreferencesKey("show_uncompleted")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val POMODORO_TIMER_ROUNDS = intPreferencesKey("pomodoro_timer_rounds")
        val POMODORO_TIMER_FOCUS_DURATION = longPreferencesKey("pomodoro_timer_focus_duration")
        val POMODORO_TIMER_SHORT_BREAK_DURATION = longPreferencesKey("pomodoro_timer_short_break_duration")
        val POMODORO_TIMER_LONG_BREAK_DURATION = longPreferencesKey("pomodoro_timer_long_break_duration")
        val USER_REWARDS_POINTS = intPreferencesKey("user_rewards_points")
        val USER_COMPLETED_REWARDS_EARNED = intPreferencesKey("user_completed_task_rewards")
        val USER_LOGIN_REWARDS_EARNED = intPreferencesKey("user_login_rewards")
        val USER_LOGIN_STREAK_REWARDS_EARNED = intPreferencesKey("user_login_streak_rewards")
    }
    override suspend fun setShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
        }
    }

    override suspend fun setShowUncompleted(showUncompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showUncompleted
        }
    }

    override suspend fun setPomodoroTimerRounds(pomodoroTimerRounds: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.POMODORO_TIMER_ROUNDS] = pomodoroTimerRounds
        }
    }

    override suspend fun setPomodoroTimerFocusDuration(pomodoroTimerFocusDuration: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.POMODORO_TIMER_FOCUS_DURATION] = pomodoroTimerFocusDuration
        }
    }

    override suspend fun setPomodoroTimerShortBreakDuration(pomodoroTimerShortBreakDuration: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.POMODORO_TIMER_SHORT_BREAK_DURATION] = pomodoroTimerShortBreakDuration
        }
    }

    override suspend fun setPomodoroTimerLongBreakDuration(pomodoroTimerLongBreakDuration: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.POMODORO_TIMER_LONG_BREAK_DURATION] = pomodoroTimerLongBreakDuration
        }
    }

    override suspend fun setSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            val currentOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )

            val newSortOrder = when(sortOrder) {
                SortOrder.NONE -> {
                    if (currentOrder == SortOrder.NONE) {
                        SortOrder.BY_DEADLINE
                    } else {
                        SortOrder.NONE
                    }
                }
                SortOrder.BY_DEADLINE -> {
                    if (currentOrder == SortOrder.BY_DEADLINE) {
                        SortOrder.BY_PRIORITY
                    } else {
                        SortOrder.BY_DEADLINE
                    }
                }
                SortOrder.BY_PRIORITY -> {
                    if (currentOrder == SortOrder.BY_PRIORITY) {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    } else {
                        SortOrder.BY_PRIORITY
                    }
                }
                SortOrder.BY_DEADLINE_AND_PRIORITY -> {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_PRIORITY) {
                        SortOrder.BY_CATEGORY
                    } else {
                        SortOrder.BY_DEADLINE_AND_PRIORITY
                    }
                }
                SortOrder.BY_CATEGORY -> {
                    if (currentOrder == SortOrder.BY_CATEGORY) {
                        SortOrder.BY_DEADLINE_AND_CATEGORY
                    } else {
                        SortOrder.BY_CATEGORY
                    }
                }
                SortOrder.BY_DEADLINE_AND_CATEGORY -> {
                    if (currentOrder == SortOrder.BY_DEADLINE_AND_CATEGORY) {
                        SortOrder.NONE
                    } else {
                        SortOrder.BY_DEADLINE_AND_CATEGORY
                    }
                }
            }

            preferences[PreferencesKeys.SORT_ORDER] = newSortOrder.name
        }
    }

    override suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_MODE] = darkMode
        }
    }

    override suspend fun updateAllPreferences(
        showCompleted: Boolean,
        sortOrder: SortOrder,
        darkMode: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
            preferences[PreferencesKeys.IS_DARK_MODE] = darkMode
        }
    }

    override suspend fun setUserRewardsPoints(userRewardsPoints: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_REWARDS_POINTS] = userRewardsPoints
        }
    }

    override suspend fun setUserRewardsEarned(userRewardsEarned: MutableMap<String, Int>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_COMPLETED_REWARDS_EARNED] = userRewardsEarned["COMPLETED_TASK_REWARD"] ?: 0
            preferences[PreferencesKeys.USER_LOGIN_REWARDS_EARNED] = userRewardsEarned["LOGIN_REWARD"] ?: 0
            preferences[PreferencesKeys.USER_LOGIN_STREAK_REWARDS_EARNED] = userRewardsEarned["LOGIN_STREAK_REWARD"] ?: 0
        }
    }

    override suspend fun getUserRewardsPoints(): Int {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.USER_REWARDS_POINTS] ?: 0
    }

    override suspend fun getUserRewardsEarned(): MutableMap<String, Int> {
        val preferences = dataStore.data.first()
        val userRewardsEarned = mutableMapOf<String, Int>()
        userRewardsEarned["COMPLETED_TASK_REWARD"] = preferences[PreferencesKeys.USER_COMPLETED_REWARDS_EARNED] ?: 0
        userRewardsEarned["LOGIN_REWARD"] = preferences[PreferencesKeys.USER_LOGIN_REWARDS_EARNED] ?: 0
        userRewardsEarned["LOGIN_STREAK_REWARD"] = preferences[PreferencesKeys.USER_LOGIN_STREAK_REWARDS_EARNED] ?: 0
        return userRewardsEarned
    }

    override suspend fun getAllPreferences(): UserPreferences {
        val preferences = dataStore.data.first()
        return UserPreferences(
            preferences[PreferencesKeys.SHOW_COMPLETED] ?: false,
            preferences[PreferencesKeys.SHOW_UNCOMPLETED] ?: false,
            SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            ),
            preferences[PreferencesKeys.IS_DARK_MODE] ?: false,
            preferences[PreferencesKeys.POMODORO_TIMER_ROUNDS] ?: 4,
            preferences[PreferencesKeys.POMODORO_TIMER_FOCUS_DURATION] ?: 25L,
            preferences[PreferencesKeys.POMODORO_TIMER_SHORT_BREAK_DURATION] ?: 5L,
            preferences[PreferencesKeys.POMODORO_TIMER_LONG_BREAK_DURATION] ?: 15L
        )
    }

    override suspend fun getShowCompleted(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
    }

    override suspend fun getShowUncompleted(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.SHOW_UNCOMPLETED] ?: false
    }

    override suspend fun getPomodoroTimerRounds(): Int {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.POMODORO_TIMER_ROUNDS] ?: 4
    }

    override suspend fun getPomodoroTimerFocusDuration(): Long {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.POMODORO_TIMER_FOCUS_DURATION] ?: 25L
    }

    override suspend fun getPomodoroTimerShortBreakDuration(): Long {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.POMODORO_TIMER_SHORT_BREAK_DURATION] ?: 5L
    }

    override suspend fun getPomodoroTimerLongBreakDuration(): Long {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.POMODORO_TIMER_LONG_BREAK_DURATION] ?: 15L
    }

    override suspend fun getSortOrder(): SortOrder {
        val preferences = dataStore.data.first()
        return SortOrder.valueOf(
            preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
        )
    }

    override suspend fun getDarkMode(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.IS_DARK_MODE] ?: false
    }

    suspend fun fetchInitialPreferences() =
        mapUserPreferences(dataStore.data.first().toPreferences())

    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get the sort order from preferences and convert it to a [SortOrder] object
        val sortOrder =
            SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            )
        val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
        val showUncompleted = preferences[PreferencesKeys.SHOW_UNCOMPLETED] ?: true
        val darkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false
        val pomodoroTimerRounds = preferences[PreferencesKeys.POMODORO_TIMER_ROUNDS] ?: 4
        val pomodoroTimerFocusDuration = preferences[PreferencesKeys.POMODORO_TIMER_FOCUS_DURATION] ?: 25L
        val pomodoroTimerShortBreakDuration = preferences[PreferencesKeys.POMODORO_TIMER_SHORT_BREAK_DURATION] ?: 5L
        val pomodoroTimerLongBreakDuration = preferences[PreferencesKeys.POMODORO_TIMER_LONG_BREAK_DURATION] ?: 15L



        return UserPreferences(
            showCompleted,
            showUncompleted,
            sortOrder,
            darkMode,
            pomodoroTimerRounds,
            pomodoroTimerFocusDuration,
            pomodoroTimerShortBreakDuration,
            pomodoroTimerLongBreakDuration
        )
    }

}