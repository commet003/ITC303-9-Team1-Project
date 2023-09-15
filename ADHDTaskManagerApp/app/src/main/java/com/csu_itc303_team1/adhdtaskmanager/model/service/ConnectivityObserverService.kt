package com.csu_itc303_team1.adhdtaskmanager.model.service

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserverService {
    fun observeConnectivity(): Flow<Status>

    enum class Status {
        CONNECTED, DISCONNECTED, LOSING, LOST
    }
}