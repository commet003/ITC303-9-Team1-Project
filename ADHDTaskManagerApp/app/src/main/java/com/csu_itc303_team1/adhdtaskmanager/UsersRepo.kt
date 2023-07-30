package com.csu_itc303_team1.adhdtaskmanager

import android.content.ContentValues.TAG
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

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
        var userExist = false
        checkUser.get()
            .addOnSuccessListener { document ->
                userExist = if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    true

                } else {
                    Log.d(TAG, "No Such Document")
                    false
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with", exception)
            }

        return userExist
    }

    fun addToFirebaseDatabase(user: Users, id: String) {
        rootRef.collection("users").document(id).set(user)
            .addOnSuccessListener {id
                Log.d(TAG, "DocumentSnapshot written with ID: ${id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun retrieveFirebaseUser(user: AuthUiClient): Users {
        var validUser: Users = Users()
        val currentUser = rootRef.collection("users").document(user.getSignedInUser()?.username.toString())
            currentUser.get().addOnSuccessListener { documentSnapshot ->
                validUser = documentSnapshot.toObject<Users>()!!
            }
        return validUser
    }
}