package com.csu_itc303_team1.adhdtaskmanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Todo::class],
    version = 1,
)
abstract class TodoDatabase: RoomDatabase() {

    abstract val todoDao: TodoDao


}