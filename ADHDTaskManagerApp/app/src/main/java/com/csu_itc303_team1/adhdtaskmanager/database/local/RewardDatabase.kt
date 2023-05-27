package com.csu_itc303_team1.adhdtaskmanager.database.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Reward::class],
    version = 1,
    exportSchema = false
)

abstract class RewardDatabase: RoomDatabase() {
    abstract val rewardDao: RewardDao
}