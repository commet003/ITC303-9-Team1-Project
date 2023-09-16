package com.csu_itc303_team1.adhdtaskmanager.data

import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LocalTaskRepository @Inject constructor(
    private val taskDao: TaskDao,
): LocalTaskDataSource {
    override suspend fun saveTask(task: Task){
        Log.d("LocalTaskRepository", "saveTask: $task")
        taskDao.upsertTask(task)
    }
    override suspend fun updateTask(task: Task){
        taskDao.upsertTask(task)
    }
    override suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

    override suspend fun getTaskByIdNonFlow(taskId: Int): Task {
        return taskDao.getTaskByIdNonFlow(taskId)
    }

    override fun getAllTasksNonFlow(): List<Task> {
        return taskDao.getAllNonFlow()
    }

    override fun getTaskById(taskId : Int): Flow<Task>{
        return flowOf(taskDao.getById(taskId))
    }
    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAll()
    }
}