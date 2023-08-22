package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context


class UsersViewModel(
    private val repo: UsersRepo = UsersRepo()
) : ViewModel() {

    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?>
        get() = _user

    private var currentUser by mutableStateOf(Users())

    private lateinit var authUiClient: AuthUiClient

    fun initializeAuthUiClient(context: Context, oneTapClient: SignInClient) {
        authUiClient = AuthUiClient(context, oneTapClient, repo)
    }

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

    fun convertToUserFromAuth(authUiClient: AuthUiClient) {
        currentUser = Users(
            displayName = authUiClient.getSignedInUser()?.username,
            points = 0,
            emailAddress = null,
            password = null,
            username = authUiClient.getSignedInUser()?.username,
            country = null,
            userID = authUiClient.getSignedInUser()?.userId
        )
    }

    fun addUserToFirebase() {
        repo.addToFirebaseDatabase(currentUser)
        _user.value = currentUser
    }

    fun completedTaskPoints() {
        val points = user.value?.points?.plus(3)
        if (points != null) {
            user.value?.let { repo.updatePoints(it, points) }
        }
    }

}
