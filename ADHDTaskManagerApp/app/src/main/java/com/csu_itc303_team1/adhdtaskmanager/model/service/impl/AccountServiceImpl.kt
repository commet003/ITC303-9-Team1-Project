package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.BuildConfig
import com.csu_itc303_team1.adhdtaskmanager.model.User
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.perf.ktx.trace
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {

    private var _isSignedIn = false

    override var oneTapClient: SignInClient? = null




    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(
                        it.uid,
                        it.photoUrl.toString(),
                        it.displayName.orEmpty(),
                        it.isAnonymous,

                    ) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticateWithGoogle(): IntentSender? {
        val result = try {
            oneTapClient?.beginSignIn(
                buildSignInRequest()
            )?.await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): FirebaseUser? {
        var user: FirebaseUser? = null
        val credential = oneTapClient!!.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        try {
            user = auth.signInWithCredential(googleCredentials).await().user
            if (user != null) {
                Log.d("AccountServiceImpl", "signInWithIntent: ${user.uid}")
            } else {
                Log.d("AccountServiceImpl", "signInWithIntent: null")
            }
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
        return user
    }

    override fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.FIREBASE_API_KEY)
                    .build()
            )
            .build()
    }

    override fun addAuthStateListener(listener: (Boolean) -> Unit) {
        auth.addAuthStateListener {
            _isSignedIn = it.currentUser != null
            listener(_isSignedIn)
        }
    }

    override suspend fun createAnonymousAccount() {
        try {
            auth.signInAnonymously().await().user
            if (auth.currentUser != null)  {
                Log.d("AccountServiceImpl", "createAnonymousAccount: ${auth.currentUser!!.uid}")

            } else {
                Log.d("AccountServiceImpl", "createAnonymousAccount: null")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override suspend fun linkAccount(): Unit =
        trace(LINK_ACCOUNT_TRACE) {
            val credential = GoogleAuthProvider.getCredential(
                auth.currentUser!!.uid,
                null
            )
            auth.currentUser!!.linkWithCredential(credential).await()
        }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()
    }

    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}



