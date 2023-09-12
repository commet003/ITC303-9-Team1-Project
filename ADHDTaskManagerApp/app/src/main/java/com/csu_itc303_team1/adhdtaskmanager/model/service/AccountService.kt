package com.csu_itc303_team1.adhdtaskmanager.model.service

import com.csu_itc303_team1.adhdtaskmanager.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    fun addAuthStateListener(listener: (Boolean) -> Unit)

    suspend fun authenticateWithGoogle()
    suspend fun createAnonymousAccount()
    suspend fun linkAccount()
    suspend fun deleteAccount()
    suspend fun signOut()
}
