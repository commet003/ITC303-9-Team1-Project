package com.csu_itc303_team1.adhdtaskmanager.utils.database_dao

import androidx.room.*
import com.csu_itc303_team1.adhdtaskmanager.utils.todo_utils.Todo
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {


    @Insert
    suspend fun insertTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Update
    suspend fun updateTodo(todo: Todo)

    // Get todo by id
    @Query("SELECT * FROM Todo WHERE id = :id")
    fun getTodoById(id: Int): Flow<Todo>

    // Get All Todos
    @Query("SELECT * FROM Todo")
    fun getAllTodos(): Flow<List<Todo>>

    // Sort Todos by Priority
    @Query("SELECT * FROM Todo ORDER BY priority DESC")
    fun sortByPriority(): Flow<List<Todo>>

    // Sort by dueDate and dueTime
    @Query("SELECT * FROM Todo ORDER BY due_date ASC, due_time ASC")
    fun sortByDueDateAndTime(): Flow<List<Todo>>

    // Sort Todos by Due Date
    //@Query("SELECT * FROM Todo ORDER BY dueDate ASC")
    //fun sortByDueDate(): Flow<List<Todo>>

    // Sort Todos by Due Time
    //@Query("SELECT * FROM Todo ORDER BY dueTime ASC")
    //fun sortByDueTime(): Flow<List<Todo>>

    // Sort Todos by isCompleted
    @Query("SELECT * FROM Todo WHERE is_completed = 1 ORDER BY completion_date ASC")
    fun sortByCompleted(): Flow<List<Todo>>

    // Sort Todos by not isCompleted
    @Query("SELECT * FROM Todo WHERE is_completed = 0")
    fun sortByNotCompleted(): Flow<List<Todo>>

    @Update
    suspend fun updateTodoIsCompleted(todo: Todo)

    @Query("SELECT COUNT(*) FROM Todo WHERE is_completed = 1")
    suspend fun getCountOfCompletedTodos(): Int

    @Query("SELECT * FROM Todo WHERE is_completed = 1")
    fun getAllCompletedTodos(): Flow<List<Todo>>
}