package com.csu_itc303_team1.adhdtaskmanager.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
): LocalTaskDataSource {
    override suspend fun saveTask(task: LocalTask){
        Log.d("LocalTaskRepository", "saveTask: $task")
        taskDao.upsertTask(task)
    }
    override suspend fun updateTask(task: LocalTask){
        taskDao.upsertTask(task)
    }
    override suspend fun deleteTask(task: LocalTask){
        taskDao.deleteTask(task)
    }
    override fun getTaskById(taskId : Int): LocalTask{
        return taskDao.getById(taskId)
    }
    override fun getAllTasks(): Flow<List<LocalTask>> {
        return taskDao.getAll()
    }
}