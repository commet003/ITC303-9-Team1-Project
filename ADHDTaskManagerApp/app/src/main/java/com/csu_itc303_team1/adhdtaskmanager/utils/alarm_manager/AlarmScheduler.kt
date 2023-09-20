package com.csu_itc303_team1.adhdtaskmanager.utils.alarm_manager

interface AlarmScheduler {
    fun scheduleAlarm(alarmItem: AlarmItem)
    fun cancelAlarm(alarmItem: AlarmItem)
}