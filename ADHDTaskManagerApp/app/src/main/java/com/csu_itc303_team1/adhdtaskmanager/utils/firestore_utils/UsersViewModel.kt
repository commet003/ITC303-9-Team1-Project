package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate


class UsersViewModel(
    private val repo: UsersRepo = UsersRepo()
) : ViewModel() {

    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?>
        get() = _user

    private var currentUser by mutableStateOf(Users())

    private lateinit var authUiClient: AuthUiClient

    fun initializeAuthUiClient(context: Context, oneTapClient: SignInClient) {
        authUiClient = AuthUiClient(context, oneTapClient, repo)
    }

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference


    fun fetchAndUpdateUserPoints(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUser(userId)  // Fetch the user. _user value will be updated after this call.

                // Wait for _user to be populated with the fetched user.
                delay(500)

                // Calculate totalPoints
                val fetchedUser = _user.value
                val totalPoints = (fetchedUser?.points ?: 0) + (fetchedUser?.loginNum ?: 0)

                // Update Firestore
                updateTotalPoints(userId, totalPoints)
            }
        }
    }

    fun loginRewardProcedure(userId: String): Boolean {
        var needReward = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUser(userId)

                delay(500)

                val lastLoginDate = _user.value?.lastLoginDate
                val currentDate = LocalDate.now()

                needReward = lastLoginDate != currentDate

            }
        }
        return needReward
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getUserTwo(userId).collect { user ->
                    _user.value = user
                }
            }
        }
    }

    fun updateUserCountry(userId: String, newCountry: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.updateCountry(userId, newCountry)
            }
        }
    }

    fun checkUserExists(id: String): Boolean {
        return repo.checkExists(id)
    }

    fun convertToUserFromAuth(authUiClient: AuthUiClient) {
        currentUser = Users(
            displayName = authUiClient.getSignedInUser()?.username,
            points = 0,
            emailAddress = null,
            password = null,
            username = authUiClient.getSignedInUser()?.username,
            country = null,
            userID = authUiClient.getSignedInUser()?.userId,
            profileImage = authUiClient.getSignedInUser()?.profilePictureUrl,
            loginNum = 0,
            totalPoints = 0,
            lastLoginDate = LocalDate.now()
        )
    }



    fun updateTotalPoints(userId: String, totalPoints: Int) {
        // Reference to the Firestore collection.
        val db = FirebaseFirestore.getInstance().collection("users")

        db.document(userId).update("totalPoints", totalPoints)
            .addOnSuccessListener {
                Log.d("Firestore", "Document successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }


    fun addUserToFirebase() {
        repo.addToFirebaseDatabase(currentUser)
        _user.value = currentUser
    }

    fun completedTaskPoints() {
        val points = user.value?.points?.plus(3)
        if (points != null) {
            user.value?.let { repo.updatePoints(it, points) }
        }
    }

    fun fetchImageList(directory: String): Flow<List<Uri>> = flow {
        val listResult = storageRef.child(directory).listAll().await()
        val uriList = mutableListOf<Uri>()
        for (reference in listResult.items) {
            val downloadUri = reference.getDownloadUrl().await() // Corrected here
            uriList.add(downloadUri)
        }
        emit(uriList)
    }

}