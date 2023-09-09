package com.csu_itc303_team1.adhdtaskmanager.service.impl

import androidx.core.os.trace
import com.csu_itc303_team1.adhdtaskmanager.DEFAULT_PROFILE_PICTURE
import com.csu_itc303_team1.adhdtaskmanager.REWARDS_COUNTS
import com.csu_itc303_team1.adhdtaskmanager.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.service.UserStorageService
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.UserData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class UserStorageServiceImpl(private val firestore: FirebaseFirestore, private val auth: AccountService) :
    UserStorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val users: Flow<List<UserData>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(USERS_COLLECTION).orderBy("rewardsPoints", Query.Direction.DESCENDING).dataObjects()
            }

    override suspend fun getUser(userId: String): UserData? =
        firestore.collection(USERS_COLLECTION).document(userId).get().await().toObject()

    override suspend fun save(user: UserData): String =
        trace(SAVE_USER_TRACE) {
            val userWithUserId = user.copy(
                userID = auth.currentUserId,
                username = auth.currentUser.toString(),
                profilePicture = DEFAULT_PROFILE_PICTURE,
                rewardsPoints = 0,
                lastLogin = Timestamp.now(),
                loginStreak = 1,
                rewardsEarned = REWARDS_COUNTS.toMutableMap()
            )
            firestore.collection(USERS_COLLECTION).add(userWithUserId).await().id
        }

    override suspend fun update(user: UserData): Unit =
        trace(UPDATE_USER_TRACE) {
            firestore.collection(USERS_COLLECTION).document(user.userID.toString()).set(user).await()
        }

    override suspend fun delete(userId: String) {
        firestore.collection(USERS_COLLECTION).document(userId).delete().await()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val SAVE_USER_TRACE = "saveUser"
        private const val UPDATE_USER_TRACE = "updateUser"
    }
}