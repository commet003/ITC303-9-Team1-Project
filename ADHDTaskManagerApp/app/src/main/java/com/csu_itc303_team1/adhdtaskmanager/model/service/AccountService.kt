package com.csu_itc303_team1.adhdtaskmanager.model.service

import android.content.Intent
import android.content.IntentSender
import com.csu_itc303_team1.adhdtaskmanager.model.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>
    var oneTapClient: SignInClient?

    fun addAuthStateListener(listener: (Boolean) -> Unit)

    suspend fun authenticateWithGoogle(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): FirebaseUser?
    fun buildSignInRequest(): BeginSignInRequest
    suspend fun createAnonymousAccount()
    suspend fun linkAccount()
    suspend fun deleteAccount()
    suspend fun signOut()
}
