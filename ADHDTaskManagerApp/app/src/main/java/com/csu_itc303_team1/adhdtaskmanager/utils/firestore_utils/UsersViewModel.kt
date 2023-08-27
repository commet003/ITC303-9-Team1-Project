package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class UsersViewModel(
    private val repo: UsersRepo = UsersRepo()
): ViewModel() {

    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?>
        get() = _user

    private var currentUser by mutableStateOf(Users())


    fun getUser(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getUserTwo(userId).collect { user ->
                    _user.value = user
                }
            }
        }
    }

    fun checkUserExists(id: String): Boolean {
        return repo.checkExists(id)
    }

    fun convertToUserFromAuth(user: AuthUiClient) {
        currentUser = Users(
            displayName = user.getSignedInUser()?.username,
            points = 0,
            emailAddress = null,
            password = null,
            username = user.getSignedInUser()?.username,
            country = null,
            userID = user.getSignedInUser()?.userId
        )

    }

    fun addUserToFirebase(){
        repo.addToFirebaseDatabase(currentUser)
        _user.value = currentUser
    }

    fun completedTaskPoints() {
        val points = user.value?.points?.plus(3)
        if (points != null) {
            user.value?.let { repo.updatePoints(it, points ) }
        }
    }

    fun checkLastLogin(): Boolean {
        val currentDate = LocalDate.now().toString()
        val lastLogin = user.value?.lastLogin.toString()
        var loggedInToday = true

        if (currentDate != lastLogin) {
            loggedInToday = false
        }
        return loggedInToday
    }

//    fun updateLastLogin() {
//        val currentDate = LocalDate.now().toString()
//        val loginStreak = user.value?.loginStreak?.plus(1)
//        user.value?.let {
//            if (loginStreak != null) {
//                repo.updateLogin(it, currentDate, loginStreak)
//            }
//        }
//    }

}