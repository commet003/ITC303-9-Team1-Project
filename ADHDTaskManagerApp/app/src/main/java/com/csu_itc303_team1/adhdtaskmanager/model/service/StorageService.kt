package com.csu_itc303_team1.adhdtaskmanager.model.service

import com.csu_itc303_team1.adhdtaskmanager.model.Task
import kotlinx.coroutines.flow.Flow

interface StorageService {
    val tasks: Flow<List<Task>>
    suspend fun getTask(taskId: String): Task?
    suspend fun save(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(taskId: String)
}
