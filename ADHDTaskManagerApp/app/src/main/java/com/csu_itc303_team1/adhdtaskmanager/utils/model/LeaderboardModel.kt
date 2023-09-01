package com.csu_itc303_team1.adhdtaskmanager.utils.model

import android.text.TextUtils
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData

data class LeaderboardModel(
    var username: String? = null,
    var rewardsPoints: Int? = 0,
    var profilePicture: String? = null,
){

    private fun randomNumberGenerator(): Int {
        return (0..100).random()
    }
    constructor(user: UserData) : this() {
        this.username = user.username.toString()
        this.rewardsPoints = user.rewardsPoints
        this.profilePicture = user.profilePicture ?: ""

        if (TextUtils.isEmpty(this.username)){
            this.username = "Anonymous${randomNumberGenerator()}"
        }
    }
}