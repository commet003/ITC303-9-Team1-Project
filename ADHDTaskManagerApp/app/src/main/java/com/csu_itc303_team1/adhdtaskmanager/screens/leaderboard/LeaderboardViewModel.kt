package com.csu_itc303_team1.adhdtaskmanager.screens.leaderboard

import androidx.lifecycle.ViewModel
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    accountService: AccountService,
    usersStorageService: UsersStorageService
): ViewModel() {
    val leaderboardUsers = usersStorageService.leaderboardUsers
    private val _currentUserId = accountService.currentUserId



    fun getCurrentUserId(): String {
        return _currentUserId
    }
}