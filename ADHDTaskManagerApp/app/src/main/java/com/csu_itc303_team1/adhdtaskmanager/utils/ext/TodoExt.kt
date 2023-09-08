package com.csu_itc303_team1.adhdtaskmanager.utils.ext

import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo


fun Todo?.hasDueDate(): Boolean {
    return this?.dueDate.orEmpty().isNotBlank()
}

fun Todo?.hasDueTime(): Boolean {
    return this?.dueTime.orEmpty().isNotBlank()
}