package com.csu_itc303_team1.adhdtaskmanager.utils.firebase

import com.google.firebase.Timestamp

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userID: String? = null,
    val username: String? = null,
    val profilePicture: String? = null,
    val rewardsPoints: Int = 0,
    val lastLogin: Timestamp = Timestamp.now(),
    val loginStreak: Int? = null,
    val rewardsEarned: MutableMap<String, Int>? = null,
)