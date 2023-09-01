package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Response(
    var leaderboardUsers: List<UserData>? = null,
    var exception: Exception? = null,
)


class Final: ArrayList<UserData>() {
    companion object {
        var finalDataList = ArrayList<UserData>()
        fun addToList(user: UserData) {
            finalDataList.add(user)
        }
    }
}



class FirestoreActivity: Activity() {

    private val db = Firebase.firestore
    private val usersRef = db.collection("users")
    private var listener: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        listener = usersRef.addSnapshotListener { snapshot, exception ->
            val response = Response()
            if (exception != null) {
                response.exception = exception
            } else {
                response.leaderboardUsers = snapshot?.toObjects(UserData::class.java)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
    }
}