package com.csu_itc303_team1.adhdtaskmanager.common.ext

import com.csu_itc303_team1.adhdtaskmanager.model.Task

fun Task?.hasDueDate(): Boolean {
    return this?.dueDate.orEmpty().isNotBlank()
}

fun Task?.hasDueTime(): Boolean {
    return this?.dueTime.orEmpty().isNotBlank()
}
