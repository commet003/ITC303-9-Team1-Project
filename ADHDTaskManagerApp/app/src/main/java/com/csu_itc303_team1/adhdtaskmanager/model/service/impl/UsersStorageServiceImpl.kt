package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import com.csu_itc303_team1.adhdtaskmanager.model.service.trace
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UsersStorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore, private val auth: AccountService):
    UsersStorageService {


    override val currentUser: Flow<FirestoreUser?>
        get() = firestore.collection(USERS_COLLECTION).document(auth.currentUserId).dataObjects()

    override val leaderboardUsers: Flow<List<FirestoreUser>>
        get() = firestore.collection(USERS_COLLECTION).orderBy("rewardPoints")
            .endAt(auth.currentUserId).dataObjects()


    override suspend fun getLeaderboardUsers(): List<FirestoreUser> {
        return firestore.collection(USERS_COLLECTION)
            .orderBy("rewardPoints").endAt(auth.currentUserId)
            .get().await().toObjects()
    }

    override suspend fun getUser(userId: String): FirestoreUser? =
        firestore.collection(USERS_COLLECTION).document(userId).get().await().toObject()

    override suspend fun save(user: FirestoreUser) {
        trace(SAVE_USER_TRACE) {
            firestore.collection(USERS_COLLECTION).document(user.id).set(user).await()
        }
    }

    override suspend fun update(user: FirestoreUser): Unit =
        trace(UPDATE_USER_TRACE) {
            firestore.collection(USERS_COLLECTION).document(user.id).set(user).await()
        }

    override suspend fun delete(userId: String) {
        firestore.collection(USERS_COLLECTION).document(userId).delete().await()
    }

    override suspend fun checkUserExistsInDatabase(userId: String): Boolean {
        val user = firestore.collection(USERS_COLLECTION).document(userId).get().await()
        return user.exists()
    }

    override fun incrementUserRewardPoints(userId: String, rewardPoints: Int, rewardCount: String) {
        firestore.collection(USERS_COLLECTION).document(userId).update("rewardPoints", FieldValue.increment(rewardPoints.toLong()),
            "rewardsEarned.$rewardCount", FieldValue.increment(1) )

    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val USERS_COLLECTION = "users"
        private const val SAVE_USER_TRACE = "saveUser"
        private const val UPDATE_USER_TRACE = "updateUser"
    }
}