package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirestoreViewModel(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val usersRef: CollectionReference = db.collection("users")
): ViewModel() {

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?>
        get() = _user

    @SuppressLint("RestrictedApi")
    private fun checkUserExists(userId: String): Boolean{
        var userExists = false
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                userExists = document != null
            }
        return userExists
    }


    fun addUserToFirestore(currentUser: UserData){
        Log.d("CurrentUser ID", currentUser.userID.toString())
        if (!checkUserExists(currentUser.userID.toString())){
            usersRef.document(currentUser.userID.toString()).set(currentUser)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $documentReference")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun getUsersFromFirestore() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
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

    private fun incrementUserLoginStreak(userId: String){
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("loginStreak", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun incrementRewardCount(userId: String, rewardName: String){
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("rewardsEarned.$rewardName", FieldValue.increment(1))
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    // Check if user last signed in more than 1 day ago
    fun checkUserLastLogin(lastLoginSeconds: Long): Boolean{
        var userLastLogin = false

        val today = Timestamp.now()
        val difference = today.seconds - lastLoginSeconds
        if (difference < 86400){
            userLastLogin = true
        }
        return userLastLogin
    }

    fun resetUserLoginStreak(userId: String) {
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("loginStreak", 1)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
    }

    fun updateLastLoginDate(userId: String){
        val washingtonRef = db.collection("users").document(userId)
        washingtonRef
            .update("lastLogin", Timestamp.now())
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        incrementUserLoginStreak(userId)
    }

    fun deleteUser(userId: String){
        db.collection("users").document(userId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

<<<<<<< HEAD
    fun updateUsername(userId: String, newUsername: String){
        val washingtonRef = usersRef.document(userId)
        washingtonRef
            .update("username", newUsername)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
=======

    fun getUsersForLeaderboard(): Flow<List<UserData>> {
        return usersRef.orderBy("name", Query.Direction.DESCENDING).limit(50).dataObjects()
>>>>>>> parent of 15a1b0c (Rewards System working)
    }
}