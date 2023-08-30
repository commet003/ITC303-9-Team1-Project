package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

/**
 * Loads a [TaskDetail] specified by the task ID.
 */
class LoadTodoDetailUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke(todoId: Long) = todoDao.loadTodoDetailById(todoId)
}