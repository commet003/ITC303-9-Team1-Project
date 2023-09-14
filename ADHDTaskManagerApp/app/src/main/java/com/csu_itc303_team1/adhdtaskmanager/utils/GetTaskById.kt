package com.csu_itc303_team1.adhdtaskmanager.utils

import com.csu_itc303_team1.adhdtaskmanager.data.LocalTaskRepository
import javax.inject.Inject

class GetTaskById @Inject constructor(
    private val localTasksRepository: LocalTaskRepository
) {
    suspend operator fun invoke(id: Int) = localTasksRepository.getTaskById(id)
}