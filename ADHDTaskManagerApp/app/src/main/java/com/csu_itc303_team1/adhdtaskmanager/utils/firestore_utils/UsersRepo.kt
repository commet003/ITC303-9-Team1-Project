package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.content.ContentValues.TAG
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.FirebaseCallback
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.properties.Delegates

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

    fun checkExists(email: String): Boolean {
        val checkUser = rootRef.collection("users").document(email)
        var userExist = true
        checkUser.get()
            .addOnSuccessListener { document ->
                userExist = if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    println("UserRepo.checkExists: User Exists")
                    true

                } else {
                    Log.d(TAG, "No Such Document")
                    println("UserRepo.checkExists: Does not exist")
                    false
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with", exception)
            }

        return userExist
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




    fun getUserTwo(userId: String): Flow<Users?> = callbackFlow {
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

        ref.update(
            "lastLogin", date
            )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)}
    }

    fun updateLoginStreak(user: Users, streak: Int) {
        val ref = userRef.document(user.userID.toString())

        ref.update(
            "loginStreak", streak
        )
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)}
    }
}