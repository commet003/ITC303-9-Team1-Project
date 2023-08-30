package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

class GetOngoingTodoSummariesUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    operator fun invoke(userId: Long) = todoDao.getOngoingTodoSummaries(userId)
}
