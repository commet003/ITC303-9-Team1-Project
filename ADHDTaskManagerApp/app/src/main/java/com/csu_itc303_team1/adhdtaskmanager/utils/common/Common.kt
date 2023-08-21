package com.csu_itc303_team1.adhdtaskmanager.utils.common


fun formatTime(seconds: String, minutes: String, hours: String): String {
    return "$minutes:$seconds"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}