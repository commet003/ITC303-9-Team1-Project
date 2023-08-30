package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

/**
 * Updates the todo status of the todos specified by the todo IDs.
 */
class UpdateTodoStatusUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke(todoIds: List<Long>, status: TodoStatus) {
        todoDao.updateTodoStatus(todoIds, status)
    }
}