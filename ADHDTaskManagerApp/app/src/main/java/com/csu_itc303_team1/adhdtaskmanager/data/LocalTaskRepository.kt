package com.csu_itc303_team1.adhdtaskmanager.data

import android.util.Log
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
    override fun getTaskById(taskId : String): LocalTask{
        return taskDao.getById(taskId)
    }
    override fun getAllTasks(): List<LocalTask> {
        return taskDao.getAll()
    }

    override fun getAllTasksByCategory(category: String): List<LocalTask> {
        return taskDao.getAllByCategory(category)
    }

    override fun getAllTasksByPriority(): List<LocalTask> {
        return taskDao.getAllByPriority()
    }

    override fun getAllTasksByDueDate(): List<LocalTask> {
        return taskDao.getAllByDueDate()
    }
}