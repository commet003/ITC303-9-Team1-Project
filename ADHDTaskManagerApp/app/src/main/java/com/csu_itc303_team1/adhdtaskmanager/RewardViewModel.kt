package com.csu_itc303_team1.adhdtaskmanager

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.database.local.Reward
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDao
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class RewardViewModel(application: Application): ViewModel() {

    val allRewards: LiveData<List<Reward>>
    private val repo: RewardRepo
    val searchResults: LiveData<List<Reward>>

    init {
        val rewardDb = RewardDatabase.getInstance(application)
        val rewardDao = rewardDb.rewardDao
        repo = RewardRepo(rewardDao)

        allRewards = repo.allRewards
        searchResults = repo.searchResults
    }

    fun insertReward(reward: Reward) {
        repo.insertReward(reward)
    }

    fun findReward(name: String) {
        repo.findReward(name)
    }

    fun updateReward(reward: Reward) {
        repo.updateReward(reward)
    }

    fun deleteReward(reward: Reward) {
        repo.deleteReward(reward)
    }

}