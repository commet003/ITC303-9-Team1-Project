package com.csu_itc303_team1.adhdtaskmanager.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.data.Todo
import com.csu_itc303_team1.adhdtaskmanager.data.TodoDetail
import com.csu_itc303_team1.adhdtaskmanager.data.TodoSummary
import com.csu_itc303_team1.adhdtaskmanager.data.TodoTag
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.data.UserTodo
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao


@Database(
    entities = [Todo::class, Tag::class, User::class, TodoTag::class, UserTodo::class],
    views = [TodoSummary::class, TodoDetail::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DBTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao
}