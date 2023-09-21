package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.content.ContentValues.TAG
import android.util.Log
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.FirebaseCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    fun updateCountry(userId: String, newCountry: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)
        userRef.update("country", newCountry)
            .addOnSuccessListener {
                // Handle successful update, maybe log it or show a toast
            }
            .addOnFailureListener { exception ->
                // Handle failure, log it or show an error
            }
    }

    fun checkExists(email: String): Boolean {
        val checkUser = rootRef.collection("users").document(email)
        var userExist = false
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

    suspend fun addToFirebaseDatabase(user: Users) {
        user.userID?.let {
            rootRef.collection("users").document(it).set(user)
                .addOnSuccessListener {user
                    Log.d(TAG, "DocumentSnapshot written with ID: ${user.userID}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
                .await()
        }
    }

    suspend fun getUserSnapshot(userId: String) {


        val ref = userRef.document(userId)

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                Log.d("User", "Looking for document")
                val user = documentSnapshot.toObject(Users::class.java)
                Log.d("User", "found document")
            }
            .addOnFailureListener {exception ->
                Log.w("User", exception)
            }
            .await()

    }


    suspend fun getUserFromFirestore(documentId: String): Users? {
        return withContext(Dispatchers.IO) {
            val docRef: DocumentReference = rootRef.collection("users").document(documentId)
            try {
                val document = docRef.get().await()
                if (document.exists()) {
                    Log.d("User", "User Exists")
                    return@withContext document.toObject(Users::class.java)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return@withContext null
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

    fun getLeaderboardThree(): ArrayList<Users> {
        val leaderboard = ArrayList<Users>()
        userRef.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            for (doc in value!!) {
                doc?.let {
                    leaderboard.add(it.toObject())
                }
            }
            Log.d(TAG, "Current cites in CA: $leaderboard")
        }
        return leaderboard
    }

    fun updatePoints(user: Users, points: Int){
        val ref = userRef.document(user.userID.toString())

        ref.update("points", points)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!")
                println("I updated Points successfully")}
            .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e)
                println("I did not update Points")}
    }

    // Add this function:
    suspend fun updateUser(uid: String, userMap: HashMap<String, *>) {
        userRef.document(uid).update(userMap as Map<String, Any>).await()
    }

}