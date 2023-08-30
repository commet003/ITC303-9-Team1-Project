package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject


/**
 * Loads all the existing tags as a list.
 */
class LoadTagsUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke() = todoDao.loadTags()
}
