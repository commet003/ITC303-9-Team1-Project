package com.csu_itc303_team1.adhdtaskmanager.database.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface RewardDao {

    @Query("SELECT * FROM Reward")
    fun getAllRewards(): List<Reward>

    @Update
    suspend fun updateReward(reward: Reward)

}