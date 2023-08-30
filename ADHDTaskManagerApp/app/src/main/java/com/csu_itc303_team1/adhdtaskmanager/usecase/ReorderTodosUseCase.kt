package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.TodoStatus
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

class ReorderTodosUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke(
        todoId: Long,
        status: TodoStatus,
        currentOrderInCategory: Int,
        targetOrderInCategory: Int
    ) {
        todoDao.reorderTodos(todoId, status, currentOrderInCategory, targetOrderInCategory)
    }
}