//package com.csu_itc303_team1.adhdtaskmanager.utils.firebase

/*import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.csu_itc303_team1.adhdtaskmanager.REWARDS_COUNTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class FirebaseAuthActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Load Todo Screen
        }
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
                        lastLogin = null,
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

    fun signIn(){

    }

    fun signOut() {
        Firebase.auth.signOut()
    }
}*/