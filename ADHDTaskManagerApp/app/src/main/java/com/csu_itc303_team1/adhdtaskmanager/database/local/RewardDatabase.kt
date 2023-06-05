package com.csu_itc303_team1.adhdtaskmanager.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Reward::class],
    version = 1,
    exportSchema = false
)

abstract class RewardDatabase: RoomDatabase() {
    abstract val rewardDao: RewardDao

    companion object {
        @Volatile
        private var INSTANCE: RewardDatabase? = null

        fun getInstance(context: Context): RewardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RewardDatabase::class.java,
                    "reward_database.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
