package com.csu_itc303_team1.adhdtaskmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.csu_itc303_team1.adhdtaskmanager.database.local.Reward
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RewardRepo(private val rewardDao: RewardDao) {

    val allRewards: LiveData<List<Reward>> = rewardDao.getAllRewards()
    val searchResults = MutableLiveData<List<Reward>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertReward(newReward: Reward) {
        coroutineScope.launch(Dispatchers.IO) {
            rewardDao.insertReward(newReward)
        }
    }

    fun updateReward(updatedReward: Reward) {
        coroutineScope.launch(Dispatchers.IO) {
            rewardDao.updateReward(updatedReward)
        }
    }

    fun deleteReward(deletedReward: Reward) {
        coroutineScope.launch(Dispatchers.IO) {
            rewardDao.deleteReward(deletedReward)
        }
    }

    fun findReward(name: String): LiveData<List<Reward>> {
        return rewardDao.findReward(name)
    }
}