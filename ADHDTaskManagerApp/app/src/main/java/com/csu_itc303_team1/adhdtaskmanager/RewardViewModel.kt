package com.csu_itc303_team1.adhdtaskmanager

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.database.local.Reward
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDao
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


class RewardViewModel(rewardDao: RewardDao): ViewModel() {

    private val repo: RewardRepo
    val allRewards: Flow<List<Reward>>

    init {
        repo = RewardRepo(rewardDao)
        allRewards = repo.rewards
    }


    fun updatePoints(reward: Reward) = viewModelScope.launch(Dispatchers.IO){
        repo.updatePoints(reward)
    }

    fun updateTimesAchieved(reward: Reward) = viewModelScope.launch(Dispatchers.IO){
        repo.updateTimesAchieved(reward)
    }

    fun deleteTodo(reward: Reward) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteReward(reward)
    }

}

data class RewardState(
    val rewards: List<Reward> = emptyList()
)