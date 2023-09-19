package com.csu_itc303_team1.adhdtaskmanager.utils.states

data class SettingsState (
    var isDarkModeEnabled: Boolean = false,
    var isMotivationAlertEnabled: Boolean = false,
    var isLeaderboardEnabled: Boolean = false
)