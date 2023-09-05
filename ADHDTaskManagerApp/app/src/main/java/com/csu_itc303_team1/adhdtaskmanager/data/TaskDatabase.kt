package com.csu_itc303_team1.adhdtaskmanager.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalTask::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}