package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import com.csu_itc303_team1.adhdtaskmanager.model.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Provides
    @Singleton
    fun provideDispatcher(): Dispatcher {
        return Dispatcher(Dispatchers.IO, Dispatchers.Main)
    }
}