package com.csu_itc303_team1.adhdtaskmanager.model.service

import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import kotlinx.coroutines.flow.Flow
interface UsersStorageService {
    val currentUser: Flow<FirestoreUser?>
    val leaderboardUsers: Flow<List<FirestoreUser>>

    suspend fun getLeaderboardUsers(): List<FirestoreUser>
    suspend fun getUser(userId: String): FirestoreUser?
    suspend fun save(user: FirestoreUser)
    suspend fun update(user: FirestoreUser)
    suspend fun delete(userId: String)

    fun incrementUserRewardPoints(userId: String, rewardPoints: Int, rewardCount: String)

    // Check if user is in database
    suspend fun checkUserExistsInDatabase(userId: String): Boolean






}