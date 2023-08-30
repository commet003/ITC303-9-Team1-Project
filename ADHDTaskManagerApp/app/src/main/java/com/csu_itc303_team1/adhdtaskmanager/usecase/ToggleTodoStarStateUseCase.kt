package com.csu_itc303_team1.adhdtaskmanager.usecase

import androidx.room.withTransaction
import com.csu_itc303_team1.adhdtaskmanager.data.User
import com.csu_itc303_team1.adhdtaskmanager.data.UserTodo
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.AppDatabase
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

/**
 * Toggles the star state for the todo.
 */
class ToggleTodoStarStateUseCase @Inject constructor(
    private val db: AppDatabase,
    private val todoDao: TodoDao = db.todoDao()
) {
    /**
     * Toggles the star state for the task.
     */
    suspend operator fun invoke(todoId: Long, currentUser: User) {
        db.withTransaction {
            val userTodo = todoDao.getUserTodo(todoId, currentUser.id)
            if (userTodo != null) {
                todoDao.deleteUserTodos(listOf(userTodo))
            } else {
                todoDao.insertUserTodos(listOf(UserTodo(userId = currentUser.id, todoId = todoId)))
            }
        }
    }
}
