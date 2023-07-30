package com.csu_itc303_team1.adhdtaskmanager

import androidx.lifecycle.ViewModel
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient

class UsersViewModel(
    private val repo: UsersRepo = UsersRepo(),
    user: AuthUiClient
): ViewModel() {

    var currentUser: Users = Users()
    var userID: String = ""

    fun checkUserExists(id: String): Boolean {
        return repo.checkExists(id)
    }

    fun convertToUserFromAuth(user: AuthUiClient) {
        currentUser = Users(
            displayName = user.getSignedInUser()?.username.toString(),
            points = 0,
            emailAddress = null,
            password = null,
            username = user.getSignedInUser()?.username.toString(),
            country = null,
        )
        userID = user.getSignedInUser()?.userId.toString()
    }

    fun addUserToFirebase(){
        repo.addToFirebaseDatabase(currentUser, userID)
    }

}