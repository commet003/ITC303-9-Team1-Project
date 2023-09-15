package com.csu_itc303_team1.adhdtaskmanager.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.csu_itc303_team1.adhdtaskmanager.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    // Get all tasks
    @Query("SELECT * FROM task_table")
    fun getAll(): Flow<List<Task>>

    @Query("SELECT * FROM task_table")
    fun getAllNonFlow(): List<Task>

    @Query("SELECT * FROM task_table WHERE id = :taskId")
    fun getTaskByIdNonFlow(taskId : String): Task

    // Get task by id
    @Query("SELECT * FROM task_table WHERE id = :id")
    fun getById(id: String): Task
    // Save a task
    @Upsert
    suspend fun upsertTask(task: Task)
    @Delete
    suspend fun deleteTask(task: Task)


    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun selectAllLogsCursor(): Cursor

    @Query("SELECT * FROM task_table WHERE id = :id")
    fun selectLogById(id: Int): Cursor?

}