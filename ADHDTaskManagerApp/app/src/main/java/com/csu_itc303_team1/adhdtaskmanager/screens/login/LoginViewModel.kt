package com.csu_itc303_team1.adhdtaskmanager.screens.login

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_REWARD
import com.csu_itc303_team1.adhdtaskmanager.LOGIN_REWARD_NAME
import com.csu_itc303_team1.adhdtaskmanager.REWARDS_COUNTS
import com.csu_itc303_team1.adhdtaskmanager.SETTINGS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.TASKS_SCREEN
import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import com.csu_itc303_team1.adhdtaskmanager.model.service.AccountService
import com.csu_itc303_team1.adhdtaskmanager.model.service.LogService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import com.csu_itc303_team1.adhdtaskmanager.screens.MainViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Duration
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService,
    private val usersStorageService: UsersStorageService,
    logService: LogService
) : MainViewModel(logService) {

    private var currentUser: FirebaseUser? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            if(currentUser != null){
                val check = usersStorageService.checkUserExistsInDatabase(accountService.currentUserId)
                if(check){
                    Log.d("LoginViewModel", "User exist in database")
                    val user = usersStorageService.getUser(accountService.currentUserId)
                    when(Duration.between(user?.lastLogin?.toDate()?.toInstant(), Date().toInstant()).toDays()) {
                        0L -> {
                            Log.d("LoginViewModel", "User logged in today")
                            user?.lastLogin = Timestamp.now()
                        }
                        1L -> {
                            if (user?.loginStreak!! < 7) {
                                user.loginStreak += 1
                            } else if (user.loginStreak == 7) {
                                user.loginStreak = 1
                                user.rewardPoints += LOGIN_REWARD
                                user.rewardsEarned[LOGIN_REWARD_NAME] = user.rewardsEarned[LOGIN_REWARD_NAME]?.plus(1) ?: 1
                            }
                            user.lastLogin = Timestamp.now()
                        }
                        else -> {
                            user?.loginStreak = 1
                            user?.lastLogin = Timestamp.now()
                        }
                    }
                    usersStorageService.update(user!!)
                } else {
                    Log.d("LoginViewModel", "User does not exist in database")
                    val newUser = FirestoreUser(
                        id = accountService.currentUserId,
                        isAnonymous = true,
                        username = generateRandomUsername(),
                        leaderboardRank = 1,
                        rewardPoints = 0,
                        lastLogin = Timestamp.now(),
                        loginStreak = 1,
                        rewardsEarned = REWARDS_COUNTS.toMutableMap()
                    )
                    usersStorageService.save(newUser)
                }
            }
            openAndPopUp(TASKS_SCREEN, SETTINGS_SCREEN)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun onSignInAnonymouslyClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.createAnonymousAccount()
            if(accountService.hasUser){
                val check = usersStorageService.checkUserExistsInDatabase(accountService.currentUserId)
                if(check){
                    Log.d("LoginViewModel", "User exist in database")
                    val user = usersStorageService.getUser(accountService.currentUserId)
                    when(Duration.between(user?.lastLogin?.toDate()?.toInstant(), Date().toInstant()).toDays()) {
                        0L -> {
                            Log.d("LoginViewModel", "User logged in today")
                            Timestamp.now()
                        }
                        1L -> {
                            if (user?.loginStreak!! < 7) {
                                user.loginStreak = user.loginStreak.plus(1)
                            } else if (user.loginStreak == 7) {
                                user.loginStreak = 1
                                user.rewardPoints = user.rewardPoints.plus(LOGIN_REWARD)
                                user.rewardsEarned[LOGIN_REWARD_NAME] = user.rewardsEarned[LOGIN_REWARD_NAME]?.plus(1) ?: 1
                            }
                            Timestamp.now()
                        }
                        else -> {
                            user?.loginStreak = 1
                            Timestamp.now()
                        }
                    }
                    usersStorageService.update(user!!)
                } else {
                    Log.d("LoginViewModel", "User does not exist in database")
                    val newUser = FirestoreUser(
                        id = accountService.currentUserId,
                        isAnonymous = true,
                        username = generateRandomUsername(),
                        leaderboardRank = 1,
                        rewardPoints = 0,
                        lastLogin = Timestamp.now(),
                        loginStreak = 1,
                        rewardsEarned = REWARDS_COUNTS.toMutableMap()
                    )
                    usersStorageService.save(newUser)
                }
            }
            openAndPopUp(TASKS_SCREEN, SETTINGS_SCREEN)
        }
    }

    suspend fun signIn() = accountService.authenticateWithGoogle()
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signInWithIntent(openAndPopUp: (String, String) -> Unit, intent: Intent){
        launchCatching {
            accountService.signInWithIntent(intent)?.let {
                currentUser = it
            }
            onSignInClick(openAndPopUp)
        }
    }

    // Function to set accountService.oneTapSignIn to oneTapClient
    fun setOneTapClient(client: SignInClient) {
        accountService.oneTapClient = client
    }
    fun authStateListener(listener: (Boolean) -> Unit) {
        accountService.addAuthStateListener(listener)
    }

    private fun generateRandomUsername(): String {
        return "Anonymous" + (0..100000).random()
    }
}