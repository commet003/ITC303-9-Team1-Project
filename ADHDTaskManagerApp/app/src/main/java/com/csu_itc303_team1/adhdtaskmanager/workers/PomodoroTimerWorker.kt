package com.csu_itc303_team1.adhdtaskmanager.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "PomodoroTimerWorker"

class PomodoroTimerWorker(ctx: Context, params: WorkerParameters):  CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        makeTimerNotification("Pomodoro Timer", applicationContext)

        return withContext(Dispatchers.IO) {
            val resolver = applicationContext.contentResolver
            return@withContext try {

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }
}