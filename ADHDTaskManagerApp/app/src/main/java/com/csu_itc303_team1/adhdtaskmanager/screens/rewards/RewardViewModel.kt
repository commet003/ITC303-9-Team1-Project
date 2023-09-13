package com.csu_itc303_team1.adhdtaskmanager.screens.rewards

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RewardViewModel @Inject constructor(
    private val accountService: AccountService,
    private val usersStorageService: UsersStorageService
): ViewModel() {

    private var _user = usersStorageService.currentUser


    fun getUser(): Flow<FirestoreUser?> {
        return _user
    }

    private fun getAccountInfo(): FirestoreUser? {
        var user: FirestoreUser? = null
        viewModelScope.launch {
            user = usersStorageService.getUser(accountService.currentUserId)
            return@launch
        }
        Log.d("RewardViewModel", "user: $user")
        return user
    }
}