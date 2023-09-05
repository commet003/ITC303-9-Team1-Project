package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.csu_itc303_team1.adhdtaskmanager.data.UserPrefRepository
import com.csu_itc303_team1.adhdtaskmanager.data.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
    // just my preference of naming including the package name
    name = "com.csu_itc303_team1.adhdtaskmanager.user_preferences"
)

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesModule {

    // binds instance of MyUserPreferencesRepository
    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ): UserPrefRepository

    companion object {

        // provides instance of DataStore
        @Provides
        @Singleton
        fun provideUserDataStorePreferences(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.userDataStore
        }
    }
}