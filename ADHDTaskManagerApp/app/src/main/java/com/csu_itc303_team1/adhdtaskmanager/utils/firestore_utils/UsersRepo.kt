package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.content.ContentValues.TAG
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.FirebaseCallback
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UsersRepo (
    // initializing the firestore database
    private val rootRef: FirebaseFirestore = FirebaseFirestore.getInstance(),
    // creating a reference to the Firestore Collection, Users
    private val userRef: CollectionReference = rootRef.collection("users")
){

    // This will either respond with the data if successful or the exception if not successful
    fun getResponse(callback: FirebaseCallback) {
        userRef.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {

                // For each document in the collection, create a user object

                val result = task.result
                result?.let {
                    response.users = result.documents.mapNotNull {snapShot ->
                        snapShot.toObject(Users::class.java)
                    }
                }
            } else {
                response.exception = task.exception
            }
            callback.onResponse(response)
        }
    }

    fun checkExists(id: String): Boolean {
        val checkUser = rootRef.collection("users").whereEqualTo("id", id)
        var userExist = false

        checkUser.get().addOnCompleteListener { task ->
            Log.d(TAG, "Checking if User Exists")
            if (task.isSuccessful) {
                for (document in task.result) {
                    val iD = document.getString("id")
                    if (iD == id) {
                        Log.d(TAG, "Found Match. User Exists")
                        userExist = true
                        break
                    }
                }
            } else if (!task.isSuccessful) {
                userExist = false
            }
        }
        return userExist
    }

    suspend fun checkUserExists(id: String) : Boolean {
        val ref = rootRef.collection("users").document(id)
        val exist: Boolean
        val doc = ref.get().await()
        exist = doc.exists()
        if (exist) {
            Log.d("CheckUser", "CheckUserExists: Found User: ${doc.id}")
        } else {
            Log.d("CheckUser", "CheckUserExists: No User Found")
        }
        return exist
    }

    fun addToFirebaseDatabase(user: Users) {
        user.userID?.let {
            rootRef.collection("users").document(it).set(user)
                .addOnSuccessListener {user
                    Log.d(TAG, "DocumentSnapshot written with ID: ${user.userID}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }




    suspend fun getUserTwo(userId: String): Flow<Users?> = callbackFlow {
        val listener = object : EventListener<DocumentSnapshot> {
            override fun onEvent(snapshot: DocumentSnapshot?, exception: FirebaseFirestoreException?) {
                if (exception != null) {
                    cancel()
                    return
                }

                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(Users::class.java)
                    trySend(user)
                }
            }
        }
        val registration = rootRef.collection("users").document(userId).addSnapshotListener(listener)
        awaitClose { registration.remove() }
    }

    // TODO: The Below Update Points Logic do work when user isn't null. I assume the other two updates should work as well

    fun updatePoints(user: Users, points: Int){
        val ref = userRef.document(user.userID.toString())

        ref.update("points", points)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
                println("I updated Points successfully")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)
                println("I did not update Points")}
    }

    fun updateLoginDate(user: Users, date: String) {
        val ref = userRef.document(user.userID.toString())
        Log.d(TAG, "Found Document. $ref")

        ref.update(
            "lastLogin", date
            )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)}
    }

    fun updateLoginStreak(user: Users, streak: Int) {
        val ref = userRef.document(user.userID.toString())
        Log.d(TAG, "Found Document. $ref")

        ref.update(
            "loginStreak", streak
        )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)}
    }
}