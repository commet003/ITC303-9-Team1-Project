package com.csu_itc303_team1.adhdtaskmanager

// Name of Notification Channel for verbose notifications of background work
val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence =
    "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION =
    "Shows notifications whenever work starts"
val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val NOTIFICATION_ID = 1

// The name of the image manipulation work
const val IMAGE_MANIPULATION_WORK_NAME = "image_manipulation_work"

const val TARGET_ACTIVITY_NAME = "com.csu_itc303_team1.adhdtaskmanager.MainActivity"
const val TASK_NOTIFICATION_CHANNEL_ID = "TASK_NOTIFICATION_CHANNEL"
const val TASK_NOTIFICATION_GROUP = "TASK_NOTIFICATION_GROUP"
const val MAX_NUM_NOTIFICATIONS = 5
const val TASK_NOTIFICATION_ID = 3
const val TASK_NOTIFICATION_SUMMARY_ID = 2
const val TASK_NOTIFICATION_REQUEST_CODE = 1

// Other keys
const val OUTPUT_PATH = "blur_filter_outputs"
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"
const val TAG_OUTPUT = "OUTPUT"
const val KEY_BLUR_LEVEL = "KEY_BLUR_LEVEL"

const val DELAY_TIME_MILLIS: Long = 3000
const val TASK_ID_EXTRA = "task_Id"

const val LOGIN_STREAK_REWARD = 1000
const val COMPLETED_TASK_REWARD = 300
const val LOGIN_REWARD = 150

const val LOGIN_STREAK_REWARD_NAME = "LOGIN_STREAK_REWARD"
const val COMPLETED_TASK_REWARD_NAME = "COMPLETED_TASK_REWARD"
const val LOGIN_REWARD_NAME = "LOGIN_REWARD"

const val SECONDS_PER_MINUTE = 60000L



val REWARDS_COUNTS = mapOf(
    "LOGIN_REWARD" to 0,
    "COMPLETED_TASK_REWARD" to 0,
    "LOGIN_STREAK_REWARD" to 0,
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