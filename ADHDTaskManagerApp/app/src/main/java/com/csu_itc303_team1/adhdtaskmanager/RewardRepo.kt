package com.csu_itc303_team1.adhdtaskmanager

import com.csu_itc303_team1.adhdtaskmanager.database.local.Reward
import com.csu_itc303_team1.adhdtaskmanager.database.local.RewardDao
import kotlinx.coroutines.flow.Flow

class RewardRepo(private val rewardDao: RewardDao) {

    val rewards: Flow<List<Reward>> = rewardDao.getAllRewards()

    suspend fun updatePoints(reward: Reward){
        rewardDao.updateReward(reward)
    }

    suspend fun updateTimesAchieved(reward: Reward) {
        rewardDao.updateReward(reward)
    }

    suspend fun deleteReward(reward: Reward){
        rewardDao.deleteReward(reward)
    }
}