package com.csu_itc303_team1.adhdtaskmanager.model.service.module

import com.csu_itc303_team1.adhdtaskmanager.notifications.Notifier
import com.csu_itc303_team1.adhdtaskmanager.notifications.SystemTrayNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {
    @Binds
    abstract fun bindNotifier(
        notifier: SystemTrayNotifier,
    ): Notifier
}