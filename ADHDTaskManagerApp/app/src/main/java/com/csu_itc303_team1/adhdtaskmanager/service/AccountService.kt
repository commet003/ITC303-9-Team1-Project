package com.csu_itc303_team1.adhdtaskmanager.service

import android.content.IntentSender
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<UserData>

    suspend fun authenticateWithGoogle() : IntentSender?
    suspend fun createAnonymousAccount()
    suspend fun linkAccount()
    suspend fun deleteAccount()
    suspend fun signOut()
}