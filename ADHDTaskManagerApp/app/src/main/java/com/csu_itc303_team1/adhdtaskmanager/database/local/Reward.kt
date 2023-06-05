package com.csu_itc303_team1.adhdtaskmanager.database.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "rewards")
data class Reward(
    val title: String,
    val description: String,
    val pointsAwarded: Int,
    val timesAchieved: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)