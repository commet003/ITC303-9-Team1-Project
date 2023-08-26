package com.csu_itc303_team1.adhdtaskmanager.utils.firebase

import android.content.Intent
import android.content.IntentSender
import com.csu_itc303_team1.adhdtaskmanager.BuildConfig
import com.csu_itc303_team1.adhdtaskmanager.REWARDS_COUNTS
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.FirestoreViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class AuthUiClient(
    firestoreViewModel: FirestoreViewModel,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private var _isSignedIn = false
    private val localFirestoreViewModel = firestoreViewModel


    // Function to addAuthStateListener to the firebase auth
    // Changes the value of _isSignedIn to true if the user is signed in
    // Changes the value of _isSignedIn to false if the user is signed out
    fun addAuthStateListener(listener: (Boolean) -> Unit) {
        auth.addAuthStateListener {
            _isSignedIn = it.currentUser != null
            if(_isSignedIn) {
                localFirestoreViewModel.addUserToFirestore(
                    UserData(
                        userID = it.currentUser?.uid,
                        username = it.currentUser?.displayName,
                        profilePicture = it.currentUser?.photoUrl?.toString(),
                        rewardsPoints = 0,
                        lastLogin = Timestamp.now(),
                        loginStreak = 1,
                        rewardsEarned = REWARDS_COUNTS.toMutableMap()
                    )
                )
            }
            listener(_isSignedIn)
        }
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
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userID = uid,
                        username = displayName,
                        profilePicture = photoUrl?.toString(),
                        rewardsPoints = 0,
                        lastLogin = Timestamp.now(),
                        loginStreak = 0,
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

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid.toString()
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
                        userID = uid,
                        username = displayName,
                        profilePicture = photoUrl?.toString(),
                        rewardsPoints = 0,
                        lastLogin = Timestamp.now(),
                        loginStreak = 0,
                        rewardsEarned = REWARDS_COUNTS.toMutableMap()
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

