package com.csu_itc303_team1.adhdtaskmanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Leaderboard View Model. Creates a User Repository

class LeaderboardViewModel(
    private val repo: UsersRepo = UsersRepo()
): ViewModel() {

    fun getResponseUsingCallback(callback: FirebaseCallback) = repo.getResponse(callback)

}