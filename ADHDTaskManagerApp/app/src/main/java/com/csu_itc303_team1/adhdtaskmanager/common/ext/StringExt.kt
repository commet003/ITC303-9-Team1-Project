package com.csu_itc303_team1.adhdtaskmanager.common.ext



fun String.idFromParameter(): String {
    return this.substring(1, this.length - 1)
}