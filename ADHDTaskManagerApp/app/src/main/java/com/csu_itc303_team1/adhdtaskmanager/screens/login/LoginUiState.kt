package com.csu_itc303_team1.adhdtaskmanager.screens.login

data class LoginUiState(
    val userId: String = "",
    val username: String = "",
    var points: Int = 0,
    val rank: Int = 1
)