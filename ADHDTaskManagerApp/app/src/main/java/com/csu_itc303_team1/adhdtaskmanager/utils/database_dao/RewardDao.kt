package com.csu_itc303_team1.adhdtaskmanager.utils.database_dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.Reward

@Dao
interface RewardDao {

    @Query("SELECT * FROM rewards")
    fun getAllRewards(): LiveData<List<Reward>>

    @Update
    suspend fun updateReward(reward: Reward)

    @Insert
    suspend fun insertReward(reward: Reward)

    @Delete
    suspend fun deleteReward(reward: Reward)

    @Query("SELECT * FROM rewards WHERE title = :name")
    fun findReward(name: String): LiveData<List<Reward>>

    @Query("SELECT * FROM rewards WHERE title = :name")
    suspend fun findRewardTwo(name: String): Reward

    @Query("SELECT * FROM rewards WHERE title = :name")
    fun findRewardThree(name: String): LiveData<Reward>

    @Query("SELECT * FROM rewards WHERE title = :name")
    suspend fun findRewardFour(name: String): List<Reward>

}