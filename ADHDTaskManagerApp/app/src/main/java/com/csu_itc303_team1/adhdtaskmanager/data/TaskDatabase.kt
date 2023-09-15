package com.csu_itc303_team1.adhdtaskmanager.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import com.csu_itc303_team1.adhdtaskmanager.model.Task

@RequiresApi(Build.VERSION_CODES.O)
@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}