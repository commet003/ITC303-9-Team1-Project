package com.csu_itc303_team1.adhdtaskmanager

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun leaderboardUsers(): ArrayList<Users> {
    val db = Firebase.firestore

    val userList = ArrayList<Users>()

    db.collection("users")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d(TAG, "${document.id} => ${document.data}")
                val user = document.toObject(Users::class.java)
                userList.add(user)
            }
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting Documents: ", exception)
        }

    return userList
}

@Composable
fun LeaderboardItem(user: Users, rank: Int) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 10.dp)
    ) {
        // Menu Icon for the Drawer Item

        Text(
            text = rank.toString(),
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(7.dp))

        Spacer(modifier = Modifier.width(7.dp))
        // Text for the Drawer Item
        Text(
            text = user.displayName.toString(),
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(7.dp))
        // Text for the Drawer Item
        Text(
            text = user.points.toString(),
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
