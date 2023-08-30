package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

class ArchiveUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke(todoIds: List<Long>) {
        todoDao.setIsArchived(todoIds, true)
    }
}