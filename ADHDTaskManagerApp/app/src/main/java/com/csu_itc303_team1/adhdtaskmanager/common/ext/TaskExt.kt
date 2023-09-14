package com.csu_itc303_team1.adhdtaskmanager.common.ext

import com.csu_itc303_team1.adhdtaskmanager.data.LocalTask

fun LocalTask?.hasDueDate(): Boolean {
    return this?.dueDate.orEmpty().isNotBlank()
}

fun LocalTask?.hasDueTime(): Boolean {
    return this?.dueTime.orEmpty().isNotBlank()
}
