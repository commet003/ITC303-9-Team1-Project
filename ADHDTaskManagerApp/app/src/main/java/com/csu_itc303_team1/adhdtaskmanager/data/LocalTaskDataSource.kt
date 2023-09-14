package com.csu_itc303_team1.adhdtaskmanager.data

import kotlinx.coroutines.flow.Flow

interface LocalTaskDataSource {
    suspend fun saveTask(task: LocalTask)
    suspend fun updateTask(task: LocalTask)
    suspend fun deleteTask(task: LocalTask)
    fun getTaskById(taskId : Int): LocalTask
    fun getAllTasks(): Flow<List<LocalTask>>
}