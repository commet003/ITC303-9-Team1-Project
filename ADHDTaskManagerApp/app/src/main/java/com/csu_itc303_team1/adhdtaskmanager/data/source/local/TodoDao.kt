package com.csu_itc303_team1.adhdtaskmanager.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.csu_itc303_team1.adhdtaskmanager.data.Tag
import com.csu_itc303_team1.adhdtaskmanager.data.Todo
import com.csu_itc303_team1.adhdtaskmanager.data.TodoDetail
import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.TodoSummary
import com.csu_itc303_team1.adhdtaskmanager.data.TodoTag
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.data.UserTodo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert
    suspend fun insertUsers(users: List<User>)

    @Insert
    suspend fun insertTags(tags: List<Tag>)

    @Insert
    suspend fun insertTodos(tasks: List<Todo>)

    @Insert
    suspend fun insertTodoTags(todoTags: List<TodoTag>)

    @Insert
    suspend fun insertUserTodos(userTasks: List<UserTodo>)

    @Delete
    suspend fun deleteUserTodos(userTodos: List<UserTodo>)

    @Query("SELECT * FROM todos")
    fun getTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM USERS WHERE id = :id")
    fun getUserById(id: Long): Flow<User?>

    @Transaction
    @Query("SELECT * FROM TodoDetail WHERE id = :id")
    fun findTodoDetailById(id: Long): Flow<TodoDetail?>

    @Transaction
    @Query("SELECT * FROM TodoDetail WHERE id = :id")
    suspend fun loadTodoDetailById(id: Long): TodoDetail?

    @Transaction
    @Query("""
        SELECT s.*,
            EXISTS(
                SELECT id
                FROM user_todos AS t
                WHERE t.todoId = s.id AND t.userId = :userId
            ) AS starred
        FROM TodoSummary AS s WHERE s.isArchived = 0
        ORDER BY s.status, s.orderInCategory
    """)
    fun getOngoingTodoSummaries(userId: Long): Flow<List<TodoSummary>>

    @Transaction
    @Query("""
        SELECT s.*,
            EXISTS(
                SELECT id
                FROM user_todos AS t
                WHERE t.todoId = s.id AND t.userId = :userId
            ) AS starred
        FROM TodoSummary AS s WHERE s.isArchived <> 0
        ORDER BY s.orderInCategory
    """)
    fun getArchivedTodoSummaries(userId: Long): Flow<List<TodoSummary>>

    @Query("UPDATE todos SET status = :status WHERE id = :id")
    suspend fun updateTodoStatus(id: Long, status: TodoStatus)

    @Query("UPDATE todos SET status = :status WHERE id IN (:ids)")
    suspend fun updateTodoStatus(ids: List<Long>, status: TodoStatus)

    @Query("UPDATE todos SET orderInCategory = :orderInCategory WHERE id = :id")
    suspend fun updateOrderInCategory(id: Long, orderInCategory: Int)

    @Query("UPDATE todos SET isArchived = :isArchived WHERE id IN (:ids)")
    suspend fun setIsArchived(ids: List<Long>, isArchived: Boolean)

    @Query("SELECT * FROM user_todos WHERE todoId = :todoId AND userId = :userId")
    suspend fun getUserTodo(todoId: Long, userId: Long): UserTodo?

    @Query("SELECT * FROM users")
    suspend fun loadUsers(): List<User>

    @Query("SELECT * FROM tags")
    suspend fun loadTags(): List<Tag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(task: Todo): Long

    @Query("SELECT tagId FROM todo_tags WHERE todoId = :todoId")
    suspend fun loadTodoTagIds(todoId: Long): List<Long>

    @Query("DELETE FROM todo_tags WHERE todoId = :todoId AND tagId IN(:tagIds)")
    suspend fun deleteTodoTags(todoId: Long, tagIds: List<Long>)

    @Query(
        """
        SELECT MIN(orderInCategory) FROM todos
        WHERE
            status = :status
            AND id <> :excludeTodoId
        """
    )
    suspend fun loadMinOrderInCategory(status: TodoStatus, excludeTodoId: Long): Int?

    @Transaction
    suspend fun saveTodoDetail(detail: TodoDetail, topOrderInCategory: Boolean) {
        if (detail.title.isEmpty()) {
            throw IllegalArgumentException("Todo must include non-empty title.")
        }
        val todo = Todo(
            id = detail.id,
            title = detail.title,
            description = detail.description,
            status = detail.status,
            creatorId = detail.creator.id,
            createdAt = detail.createdAt,
            dueAt = detail.dueAt,
            isArchived = detail.isArchived,
            orderInCategory = if (topOrderInCategory) {
                val min = loadMinOrderInCategory(detail.status, detail.id)
                if (min == null) 1 else min - 1
            } else {
                detail.orderInCategory
            }
        )
        val todoId = insertTodo(todo)
        val updatedTagIds = detail.tags.map { tag -> tag.id }
        val currentTagIds = loadTodoTagIds(todoId)
        val removedTagIds = currentTagIds.filter { id -> id !in updatedTagIds }
        deleteTodoTags(todoId, removedTagIds)
        val newTagIds = updatedTagIds.filter { id -> id !in currentTagIds }
        insertTodoTags(newTagIds.map { id -> TodoTag(todoId = todoId, tagId = id) })
    }

    @Query(
        """
        UPDATE todos
        SET orderInCategory = orderInCategory + :delta
        WHERE
            status = :status
            AND orderInCategory BETWEEN :minOrderInCategory AND :maxOrderInCategory
        """
    )
    suspend fun shiftTodos(
        status: TodoStatus,
        minOrderInCategory: Int,
        maxOrderInCategory: Int,
        delta: Int
    )

    @Transaction
    suspend fun reorderTodos(
        todoId: Long,
        status: TodoStatus,
        currentOrderInCategory: Int,
        targetOrderInCategory: Int
    ) {
        if (currentOrderInCategory < targetOrderInCategory) {
            shiftTodos(status, currentOrderInCategory + 1, targetOrderInCategory, -1)
        } else {
            shiftTodos(status, targetOrderInCategory, currentOrderInCategory - 1, 1)
        }
        updateOrderInCategory(todoId, targetOrderInCategory)
    }
}
