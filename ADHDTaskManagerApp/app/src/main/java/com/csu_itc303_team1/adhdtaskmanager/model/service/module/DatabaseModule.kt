package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import android.content.Context
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.data.TaskDao
import com.csu_itc303_team1.adhdtaskmanager.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


const val TASK_DATABASE= "task_database"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "$TASK_DATABASE.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }
}