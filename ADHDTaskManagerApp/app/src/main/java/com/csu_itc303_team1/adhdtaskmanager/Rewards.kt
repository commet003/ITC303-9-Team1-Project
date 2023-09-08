package com.csu_itc303_team1.adhdtaskmanager

const val SIGN_IN_REWARD = 150
const val TASK_COMPLETED_REWARD = 300
const val WEEK_LONG_STREAK_REWARD = 1000

const val SIGN_IN_REWARD_NAME = "SIGN_IN_REWARD"
const val TASK_COMPLETED_REWARD_NAME = "TASK_COMPLETED_REWARD"
const val WEEK_LONG_STREAK_REWARD_NAME = "WEEK_LONG_STREAK_REWARD"

val REWARDS = mapOf(
    SIGN_IN_REWARD_NAME to SIGN_IN_REWARD,
    TASK_COMPLETED_REWARD_NAME to TASK_COMPLETED_REWARD,
    WEEK_LONG_STREAK_REWARD_NAME to WEEK_LONG_STREAK_REWARD,
)


val REWARDS_COUNTS = mapOf(
    "SIGN_IN_REWARD" to 0,
    "TASK_COMPLETED_REWARD" to 0,
    "WEEK_LONG_STREAK_REWARD" to 0,
)

val LEVELS = mapOf<Int, Int>(
    1 to 1500,
    2 to 3000,
    3 to 6000,
    4 to 12000,
    5 to 20000,
    6 to 30000,
    7 to 50000,
    8 to 75000,
    9 to 100000,
    10 to 150000,
)

const val DEFAULT_PROFILE_PICTURE = "https://firebasestorage.googleapis.com/v0/b/adhdtaskmanager-d532d.appspot.com/o/default-user-profile-picture%2FUntitled.png?alt=media&token=0461fb17-8ef2-4192-9c9d-25dfacfd7420"