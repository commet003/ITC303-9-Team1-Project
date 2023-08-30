package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

class FindTodoDetailUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    operator fun invoke(todoId: Long) = todoDao.findTodoDetailById(todoId)
}