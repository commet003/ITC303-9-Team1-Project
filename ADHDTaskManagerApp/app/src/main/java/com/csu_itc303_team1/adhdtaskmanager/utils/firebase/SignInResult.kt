package com.csu_itc303_team1.adhdtaskmanager.utils.firebase

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
<<<<<<< HEAD
    val userID: String? = null,
    val username: String? = null,
    val profilePicture: String? = null,
    val rewardsPoints: Int = 0,
    val lastLogin: Timestamp = Timestamp.now(),
    val loginStreak: Int? = null,
    val rewardsEarned: MutableMap<String, Int>? = null,
=======
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
>>>>>>> parent of 25e01d5 (Simplified Firestore and Rewards systems)
)