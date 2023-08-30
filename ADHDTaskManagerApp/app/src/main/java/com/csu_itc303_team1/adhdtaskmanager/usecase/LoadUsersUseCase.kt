package com.csu_itc303_team1.adhdtaskmanager.usecase

import com.csu_itc303_team1.adhdtaskmanager.data.source.local.TodoDao
import javax.inject.Inject

/**
 * Loads all the existing users.
 */
class LoadUsersUseCase @Inject constructor(
    private val todoDao: TodoDao
) {
    suspend operator fun invoke() = todoDao.loadUsers()
}
