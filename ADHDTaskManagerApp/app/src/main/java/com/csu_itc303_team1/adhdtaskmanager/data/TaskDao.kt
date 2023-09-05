package com.csu_itc303_team1.adhdtaskmanager.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TaskDao {
    // Get all tasks
    @Query("SELECT * FROM task_table")
    fun getAll(): List<LocalTask>

    // Get task by id
    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getById(id: String): LocalTask

    // Get all tasks with a specific category
    @Query("SELECT * FROM task_table WHERE task_category = :category")
    fun getAllByCategory(category: String): List<LocalTask>

    // Order all tasks by priority
    @Query("SELECT * FROM task_table ORDER BY task_priority DESC")
    fun getAllByPriority(): List<LocalTask>

    // Order all tasks by dueDate and dueTime
    @Query("SELECT * FROM task_table ORDER BY task_dueDate, task_dueTime DESC")
    fun getAllByDueDate(): List<LocalTask>

    // Save a task
    @Upsert
    suspend fun upsertTask(task: LocalTask)
    @Delete
    suspend fun deleteTask(task: LocalTask)


    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun selectAllLogsCursor(): Cursor

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun selectLogById(id: Int): Cursor?

}