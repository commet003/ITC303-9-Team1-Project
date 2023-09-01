package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import android.annotation.SuppressLint
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Locale

open class LeaderboardAdapter(query: Query) : FirestoreAdapter(query) {



    companion object {
        @SuppressLint("ConstantLocale")
        private val FORMAT = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault(),
        )
    }
}