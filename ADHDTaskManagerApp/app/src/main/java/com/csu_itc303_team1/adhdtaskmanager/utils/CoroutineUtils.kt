package com.csu_itc303_team1.adhdtaskmanager.utils

import kotlinx.coroutines.flow.SharingStarted


private const val STOP_TIMEOUT_MILLIS: Long = 5000

val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS)
