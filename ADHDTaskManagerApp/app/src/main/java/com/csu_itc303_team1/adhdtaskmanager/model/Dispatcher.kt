package com.csu_itc303_team1.adhdtaskmanager.model

import kotlinx.coroutines.CoroutineDispatcher

data class Dispatcher(
    val iO: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)