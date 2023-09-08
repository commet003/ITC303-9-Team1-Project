package com.csu_itc303_team1.adhdtaskmanager.ui.settings_screen

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    // Using SharedPreferences to store the theme preference
    private val prefs: SharedPreferences = application.getSharedPreferences(
        "settings_prefs", Context.MODE_PRIVATE
    )

    // LiveData to represent the dark theme state
    val isDarkTheme: MutableLiveData<Boolean> = MutableLiveData(
        prefs.getBoolean("isDarkTheme", false)
    )

    // Toggle the theme preference and save it
    fun toggleTheme(isDark: Boolean) {
        isDarkTheme.value = isDark
        with(prefs.edit()) {
            putBoolean("isDarkTheme", isDark)
            apply()
        }
    }
}