package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.TodoDetail
import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

/**
 * Saves the specified [TodoDetail].
 */
class SaveTodoDetailUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke(detail: TodoDetail, topOrderInCategory: Boolean) {
        todoDao.saveTodoDetail(detail, topOrderInCategory)
    }
}