package com.csu_itc303_team1.adhdtaskmanager

data class Response(
    var users: List<Users>? = null,
    var exception: Exception? = null,
    var user: Users? = null
)

data class Users (
    val displayName: String? = null,
    var points: Int? = null,
    val emailAddress: String? = null,
    val password: String? = null,
    val username: String? = null,
    val country: String? = null,
    val userID: String? = null
        )

// Final list of users ready for displaying
class Final: ArrayList<Users>() {
    companion object {
        var finalDataList = ArrayList<Users>()
        fun addToList(user: Users) {
            finalDataList.add(user)
        }
    }
}