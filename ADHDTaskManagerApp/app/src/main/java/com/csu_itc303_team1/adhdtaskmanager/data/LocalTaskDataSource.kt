package com.csu_itc303_team1.adhdtaskmanager.data

import com.csu_itc303_team1.adhdtaskmanager.model.Task
import kotlinx.coroutines.flow.Flow

interface LocalTaskDataSource {
    suspend fun saveTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    fun getTaskById(taskId : Int): Flow<Task>

    suspend fun getTaskByIdNonFlow(taskId : Int): Task

    fun getAllTasksNonFlow(): List<Task>
    fun getAllTasks(): Flow<List<Task>>
}