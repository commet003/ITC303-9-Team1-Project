package com.csu_itc303_team1.adhdtaskmanager.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
