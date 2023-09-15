package com.csu_itc303_team1.adhdtaskmanager.model

import java.time.LocalDateTime

data class AlarmItem(
    val id: Long,
    val time: LocalDateTime,
    val title: String,
    val description: String,
)