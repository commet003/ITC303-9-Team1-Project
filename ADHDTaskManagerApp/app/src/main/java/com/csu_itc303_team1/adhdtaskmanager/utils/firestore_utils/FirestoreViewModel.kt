package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.SIGN_IN_REWARD
import com.csu_itc303_team1.adhdtaskmanager.WEEK_LONG_STREAK_REWARD
import com.csu_itc303_team1.adhdtaskmanager.WEEK_LONG_STREAK_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.FirebaseCallback
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

class FirestoreViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val usersRef: CollectionReference = db.collection("users")
): ViewModel() {

    private val _user = MutableStateFlow<UserData?>(null)
    private val _userExists = MutableStateFlow(false)

    val userExists: StateFlow<Boolean>
        get() = _userExists

    val user: StateFlow<UserData?>
        get() = _user

    fun getResponse(callback: FirebaseCallback) {
        usersRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                // For each document in the collection, create a user object
                val result = task.result
                result?.let {
                    response.leaderboardUsers = result.documents.mapNotNull {snapShot ->
                        snapShot.toObject(UserData::class.java)
                    }
                }
            } else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }


    @SuppressLint("RestrictedApi")
    suspend fun checkUserExists(userId: String): Boolean{
        usersRef.document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    _user.value = document.toObject(UserData::class.java)
                    _userExists.value = true
                } else {
                    Log.d(TAG, "No such document")
                    _userExists.value = false
                }
            }.await().data
        return _userExists.value
    }


    fun addUserToFirestore(){
        Log.d("CurrentUser ID", _user.value.toString())
            usersRef.document(_user.value?.userID.toString()).set(_user.value!!)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }


    fun getUser(userId: String): UserData? {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUserFromFirestore(userId).collect {
                    _user.value = it
                }
            }
        }

        return _user.value
    }


    private fun getUserFromFirestore(userId: String): Flow<UserData?> {
        return callbackFlow {
            val listener = object : EventListener<DocumentSnapshot> {
                override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                    if (exception != null) {
                        cancel()
                        return
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val user = snapshot.toObject(UserData::class.java)
                        trySend(user)
                    }
                }
            }
            val registration = usersRef.document(userId).addSnapshotListener(listener)
            awaitClose { registration.remove() }
        }
    }

    fun updateUserRewardsPoints(rewardPoints: Int, userId: String){
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("rewardsPoints", FieldValue.increment(rewardPoints.toLong()))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    private suspend fun incrementUserLoginStreak(userId: String){
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("loginStreak", FieldValue.increment(1),
                "rewardsPoints", FieldValue.increment(SIGN_IN_REWARD.toLong()),
                "rewardsEarned.SIGN_IN_REWARD", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }.await()
    }

    suspend fun incrementRewardCount(userId: String, rewardName: String){
        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("rewardsEarned.$rewardName", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }.await()
    }

    private suspend fun resetUserLoginStreak(userId: String) {
        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("loginStreak", 1)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }.await()
    }

    // Check that the users last login was not today or more than one day ago
    private suspend fun checkLastLogin(userId: String): Boolean{
        var lastLogin = false
        val washingtonRef = usersRef.document(userId)
        washingtonRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject(UserData::class.java)
                    if (user != null) {
                        val lastLoginDate = user.lastLogin
                        val today = Timestamp.now().seconds
                        if (lastLoginDate != null) {
                            val lastLoginDateSeconds = lastLoginDate.seconds
                            val difference = today - lastLoginDateSeconds
                            if (difference in 86401..172799){
                                lastLogin = true
                            } else if (difference > 172800){
                                viewModelScope.launch {
                                    resetUserLoginStreak(userId)
                                }
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }.await().data
        return lastLogin
    }

    // Check if login streak is greater than or equal to 7
    private suspend fun checkLoginStreak(userId: String): Boolean{
        var loginStreak = false
        val washingtonRef = usersRef.document(userId)
        washingtonRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject(UserData::class.java)
                    if (user != null) {
                        val loginStreakCount = user.loginStreak
                        if (loginStreakCount != null) {
                            if (loginStreakCount >= 7){
                                loginStreak = true
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }.await().data
        return loginStreak
    }


    suspend fun updateLastLoginDate(userId: String){

        if (checkLastLogin(userId)){
            incrementUserLoginStreak(userId)
        }
        if (checkLoginStreak(userId)){
            updateUserRewardsPoints(userId = userId, rewardPoints = WEEK_LONG_STREAK_REWARD)
            incrementRewardCount(userId = userId, rewardName = WEEK_LONG_STREAK_REWARD_NAME)
            resetUserLoginStreak(userId)
        }

        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("lastLogin", Timestamp(Date()))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }.await()
    }

    fun deleteUser(userId: String){
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun updateUsername(userId: String, newUsername: String){
        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("username", newUsername)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    // Get user profile picture from cloud storage
    fun updateUserProfilePicture(userId: String, newProfilePicture: String){
        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("profilePicture", newProfilePicture)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }
}