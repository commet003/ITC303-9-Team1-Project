package com.csu_itc303_team1.adhdtaskmanager.screens.leaderboard

import androidx.lifecycle.ViewModel
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    accountService: AccountService,
    usersStorageService: UsersStorageService,
    connectivityObserverService: ConnectivityObserverService
): ViewModel() {
    val leaderboardUsers = usersStorageService.leaderboardUsers
    private val _currentUserId = accountService.currentUserId

    val connectivityObserver = connectivityObserverService.observeConnectivity()


    // Function to observe the current connectivity status


    fun getCurrentUserId(): String {
        return _currentUserId
    }
}