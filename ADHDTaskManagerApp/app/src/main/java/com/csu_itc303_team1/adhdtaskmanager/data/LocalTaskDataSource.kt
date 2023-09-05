package com.csu_itc303_team1.adhdtaskmanager.data

interface LocalTaskDataSource {
    suspend fun saveTask(task: LocalTask)
    suspend fun updateTask(task: LocalTask)
    suspend fun deleteTask(task: LocalTask)
    fun getTaskById(taskId : String): LocalTask
    fun getAllTasks(): List<LocalTask>
    fun getAllTasksByCategory(category: String): List<LocalTask>
    fun getAllTasksByPriority(): List<LocalTask>
    fun getAllTasksByDueDate(): List<LocalTask>
}