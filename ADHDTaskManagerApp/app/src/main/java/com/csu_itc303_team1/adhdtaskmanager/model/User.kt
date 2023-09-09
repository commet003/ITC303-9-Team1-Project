package com.csu_itc303_team1.adhdtaskmanager.model

import com.google.firebase.Timestamp


data class User(
    val id: String = "",
    val isAnonymous: Boolean = true
)

data class FirestoreUser(
    val id: String = "",
    val isAnonymous: Boolean = true,
    var username: String = "",
    var leaderboardRank: Int = 1,
    var rewardPoints: Int = 0,
    var lastLogin: Timestamp = Timestamp.now(),
    var loginStreak: Int = 1,
    val rewardsEarned: MutableMap<String, Int> = mutableMapOf()
)