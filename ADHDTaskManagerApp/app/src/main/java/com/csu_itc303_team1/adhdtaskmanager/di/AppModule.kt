package com.csu_itc303_team1.adhdtaskmanager.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.AppDatabase
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private lateinit var appDatabase: AppDatabase
    private lateinit var clock: Clock

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        appDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo-db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    GlobalScope.launch(Dispatchers.IO) {
                    }
                }
            })
            .build()
        return appDatabase
    }

   /* @Provides
    fun provideCurrentUser(): User {
        // For simplicity of sample, assign first user as current user
        return SeedData.Users[0]
    }*/

    @Provides
    fun provideTodoDao(appDatabase: AppDatabase): TodoDao {
        return appDatabase.todoDao()
    }

    @Provides
    @Singleton
    fun provideClock(): Clock {
        clock = Clock.systemDefaultZone()
        return clock
    }
}