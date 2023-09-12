package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskDataSource
import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskRepository
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConfigurationService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.PomodoroTimerService
import com.csu_itc303_team1.adhdtaskmanager.model.service.StorageService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UserPreferencesService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.AccountServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.ConfigurationServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.LogServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.PomodoroTimerServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.StorageServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.UserPreferencesServiceImpl
import com.csu_itc303_team1.adhdtaskmanager.model.service.impl.UsersStorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun bindLocalTaskDatabase(impl: LocalTaskRepository): LocalTaskDataSource

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun providesUsersStorageService(impl: UsersStorageServiceImpl): UsersStorageService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService

    @Binds
    abstract fun providePomoTimerService(impl: PomodoroTimerServiceImpl): PomodoroTimerService

    @Binds
    abstract fun userPreferencesService(impl: UserPreferencesServiceImpl): UserPreferencesService
}