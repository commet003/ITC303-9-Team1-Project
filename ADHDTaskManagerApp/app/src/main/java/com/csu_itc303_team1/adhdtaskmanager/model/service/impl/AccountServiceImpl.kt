package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.app.Activity
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.model.User
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
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

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous
                    ) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticateWithGoogle() {

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

        // Sign the user back in anonymously.
        //createAnonymousAccount()
    }

    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
    }
}

class SignInWithGoogleActivity() : Activity(){

}


