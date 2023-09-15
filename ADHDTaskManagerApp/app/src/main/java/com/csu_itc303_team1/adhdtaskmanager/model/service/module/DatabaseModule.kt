package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.csu_itc303_team1.adhdtaskmanager.BuildConfig
import com.csu_itc303_team1.adhdtaskmanager.data.TaskDao
import com.csu_itc303_team1.adhdtaskmanager.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton


const val TASK_DATABASE= "task_database"

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        val factory = SupportFactory(SQLiteDatabase.getBytes(BuildConfig.DATABASE_PASSWORD.toCharArray()))
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "$TASK_DATABASE.db"
        ).openHelperFactory(factory).fallbackToDestructiveMigration().build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

    @Singleton
    @Provides
    fun provideAppContext(@ApplicationContext context: Context) = context
}