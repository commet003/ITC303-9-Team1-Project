package com.csu_itc303_team1.adhdtaskmanager.utils.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.csu_itc303_team1.adhdtaskmanager.BuildConfig
import com.csu_itc303_team1.adhdtaskmanager.MainActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.api.AuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthUiClient(
    private val activity: Activity,
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private lateinit var credential: SignInCredential
    private lateinit var googleIdToken: String
    private lateinit var googleCredentials: AuthCredential

    private val auth = Firebase.auth
    private var _isSignedIn = false

    // Function to addAuthStateListener to the firebase auth
    // Changes the value of _isSignedIn to true if the user is signed in
    // Changes the value of _isSignedIn to false if the user is signed out
    fun addAuthStateListener(listener: (Boolean) -> Unit) {
        auth.addAuthStateListener {
            _isSignedIn = it.currentUser != null
            listener(_isSignedIn)
        }
    }


    // Public getter fun for _isSignedIn
    fun getSignedIn(): Boolean {
        return _isSignedIn
    }



    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        credential = oneTapClient.getSignInCredentialFromIntent(intent)
        googleIdToken = credential.googleIdToken!!
        googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    fun getUsersGoogleCredentials(intent: Intent): AuthCredential{
        val convertorGoogleUser = oneTapClient.getSignInCredentialFromIntent(intent)
        val convertorGoogleIdToken = convertorGoogleUser.googleIdToken!!
        return GoogleAuthProvider.getCredential(convertorGoogleIdToken, null)
    }

    fun convertAnonymousUserToGoogleUser(authCredential: AuthCredential) {
        auth.currentUser!!.linkWithCredential(authCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Convert Anon User to a Google User", "linkWithCredential:success")
                    val user = task.result?.user
                    Toast.makeText(
                        context,
                        "Authentication successful.",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Log.w("Convert Anon User to a Google User", "linkWithCredential:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }



    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()
            )
            .build()
    }

    // private function to allow the user to sign in anonymously
    suspend fun signInAnonymously(): SignInResult {
        return try {
            val user = auth.signInAnonymously().await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    // Update anonymous users profile username
    suspend fun updateUsername(username: String): SignInResult {
        return try {
            val user = auth.currentUser
            user?.updateProfile(
                com.google.firebase.auth.UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
            )?.await()
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    // Get is user anonymous
    fun isUserAnonymous(): Boolean {
        return auth.currentUser?.isAnonymous ?: false
    }

}

