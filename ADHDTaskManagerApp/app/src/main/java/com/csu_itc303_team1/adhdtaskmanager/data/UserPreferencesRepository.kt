package com.csu_itc303_team1.adhdtaskmanager.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
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
    val sortOrder: SortOrder = SortOrder.NONE,
    val darkMode: Boolean = false
)

// domain layer
interface UserPrefRepository {

    suspend fun setShowCompleted(
        showCompleted: Boolean
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

    suspend fun getAllPreferences(): UserPreferences

    suspend fun getShowCompleted(): Boolean

    suspend fun getSortOrder(): SortOrder

    suspend fun getDarkMode(): Boolean
}

/**
 * Class that handles saving and retrieving user preferences
 */
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPrefRepository {

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }
    override suspend fun setShowCompleted(showCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHOW_COMPLETED] = showCompleted
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
            preferences[PreferencesKeys.DARK_MODE] = darkMode
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
            preferences[PreferencesKeys.DARK_MODE] = darkMode
        }
    }

    override suspend fun getAllPreferences(): UserPreferences {
        val preferences = dataStore.data.first()
        return UserPreferences(
            preferences[PreferencesKeys.SHOW_COMPLETED] ?: false,
            SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
            ),
            preferences[PreferencesKeys.DARK_MODE] ?: false
        )
    }

    override suspend fun getShowCompleted(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
    }

    override suspend fun getSortOrder(): SortOrder {
        val preferences = dataStore.data.first()
        return SortOrder.valueOf(
            preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name
        )
    }

    override suspend fun getDarkMode(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.DARK_MODE] ?: false
    }


}