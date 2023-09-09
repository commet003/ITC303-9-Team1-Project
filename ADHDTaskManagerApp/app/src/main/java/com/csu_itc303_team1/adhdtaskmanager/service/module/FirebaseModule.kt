package com.csu_itc303_team1.adhdtaskmanager.service.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseModule {
    fun auth(): FirebaseAuth = Firebase.auth

    fun firestore(): FirebaseFirestore = Firebase.firestore
}