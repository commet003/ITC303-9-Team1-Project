package com.csu_itc303_team1.adhdtaskmanager.service

import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import kotlinx.coroutines.flow.Flow

interface UserStorageService {
    val users: Flow<List<UserData>>
    suspend fun getUser(userId: String): UserData?
    suspend fun save(user: UserData): String
    suspend fun update(user: UserData)
    suspend fun delete(userId: String)
}