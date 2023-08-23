package com.csu_itc303_team1.adhdtaskmanager.utils.userRewardViewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.ui.reward_screen.RewardRepo
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.Users
import com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils.UsersRepo
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.Reward
import com.csu_itc303_team1.adhdtaskmanager.utils.local_database.RewardDatabase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class UserRewardViewModel(
    application: Application,
    private val userRepo: UsersRepo = UsersRepo()
    ): ViewModel()  {

    // From UserViewModel
    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?>
        get() = _user

    private var currentUser by mutableStateOf(Users())

    // From RewardViewModel
    var allRewards: LiveData<List<Reward>>
    private val rewardRepo: RewardRepo
    private var _searchResults = MutableLiveData<List<Reward>>()
    var searchResults: LiveData<List<Reward>> = _searchResults
    var activeReward: Reward? = searchResults.value?.get(0)

    private var searchedReward: List<Reward> = listOf()

    init {
        val rewardDb = RewardDatabase.getInstance(application)
        val rewardDao = rewardDb.rewardDao
        rewardRepo = RewardRepo(rewardDao)

        allRewards = rewardRepo.allRewards
    }


    suspend fun checkUserExists(googleUser: AuthUiClient) {
        viewModelScope.launch(Dispatchers.IO) {

            val exists = googleUser.getSignedInUser()?.userId?.let { userRepo.checkUserExists(it) }
            val reward = findRewardFive("Log In Reward")[0]
            println("Check User Exists. The User: $exists")

            // The line below checks if the current user exists in the user database
            // returns true if the user exists and false if the user doesn't exist

            if (reward.title != "Log In Reward") {
                println("Could not find reward in Reward Database Fucking Bullshit")
            } else {

                when (exists) {
                    true -> {
                        // If the user exists they are converted from Firebase Auth User
                        // to User
                        _user.value = convertToUserFromAuth(googleUser)

                        println("I guess user Exists. Ran get user code. User: ${user.value}")
                        checkLastLogin(reward)
                    }
                    else -> {
                        println("userRewardViewModel: User Doesn't exist, adding user")

                        // the line below sets both _user and currentUser to the
                        // current signed in user, removing the need for the getUser function
                        _user.value = convertToUserFromAuth(googleUser)
                        addUserToFirebase()
                        checkLastLogin(reward)
                    }
                }
            }
        }
    }

    // Changed the convertToUserFromAuth function to return a User object
    // The function will still set the currentUser variable to the current user
    private fun convertToUserFromAuth(user: AuthUiClient): Users {
         currentUser =  Users(
            displayName = user.getSignedInUser()?.username,
            points = 0,
            emailAddress = null,
            password = null,
            username = user.getSignedInUser()?.username,
            country = null,
            userID = user.getSignedInUser()?.userId
        )
        return currentUser
    }

    private fun addUserToFirebase(){
        userRepo.addToFirebaseDatabase(currentUser)
        _user.value = currentUser
    }

    fun completedTaskPoints() {
        val points = user.value?.points?.plus(3)
        if (points != null) {
            user.value?.let { userRepo.updatePoints(it, points ) }
        }
    }

    private fun checkLastLogin(loginReward: Reward) {
        val currentDate = LocalDate.now().toString()
        val lastLoginDate = user.value?.lastLogin.toString()
        println("checking last log in")
        println(user.value.toString())

        if (currentDate != lastLoginDate) {
            updateLogInStreak()
            updateLastLogin()
            updateLogInReward(loginReward)
            println("User hasn't logged in today before. Awarding Points and updating streak")
        } else {
            println("User has already logged in today")
        }

    }

    private fun updateLastLogin() {
        viewModelScope.launch(Dispatchers.IO){
            val currentDate = LocalDate.now().toString()
            user.value?.let { userRepo.updateLoginDate(it, currentDate) }
            println("UpdateLastLogin: logged today's date as last Logged in.")
        }
    }

    private fun updateLogInStreak() {
        viewModelScope.launch(Dispatchers.IO){
            val yesterdaysDate = LocalDate.now().minusDays(1).toString()
            val recordedDate = user.value?.lastLogin

            if (recordedDate != yesterdaysDate) {
                val newStreak = 1
                user.value?.let { userRepo.updateLoginStreak(it, newStreak) }
                println("Updated log in streak to $newStreak")
            } else {
                val newStreak = user.value?.loginStreak?.plus(1)
                user.value?.let {
                    if (newStreak != null) {
                        userRepo.updateLoginStreak(it, newStreak)
                        println("Updated log in streak to $newStreak")
                    }
                }
            }
        }
    }

    // Reward ViewModel

    fun insertReward(reward: Reward) {
        rewardRepo.insertReward(reward)
    }

    fun findReward(name: String): LiveData<List<Reward>> {
        viewModelScope.launch(Dispatchers.IO) {
            searchResults = rewardRepo.findReward(name)
        }
        return searchResults
    }

    fun updateReward(reward: Reward) {
        rewardRepo.updateReward(reward)
    }

    fun deleteReward(reward: Reward) {
        rewardRepo.deleteReward(reward)
    }

    fun updateState(rewardName: String) {
        findReward(rewardName)
    }

    private fun updateLogInReward(reward: Reward) {
        viewModelScope.launch(Dispatchers.IO) {
            if (reward.title != "Log In Reward") {
                println("updateLogInReward: loginReward Variable equals Null")
            } else {
                reward.timesAchieved = reward.timesAchieved.plus(1)
                updateReward(reward)
                println("Updated Log In Reward in Reward Database")
            }
        }
    }

    suspend fun findRewardTwo(name: String): Reward {
        val deferred: Deferred<Reward> = viewModelScope.async {
            rewardRepo.findRewardTwo(name)
        }
        return deferred.await()
    }

    fun findRewardThree(name: String): LiveData<Reward> {
        return rewardRepo.findRewardThree(name)
    }

    suspend fun findRewardFour(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchedReward = rewardRepo.findRewardFour(name)
            println(searchedReward[0])
        }
    }

    private suspend fun findRewardFive(name: String): List<Reward> {
        var list: List<Reward> = listOf()
        val please: Deferred<Unit> = viewModelScope.async {
            list = rewardRepo.findRewardFour(name)
            println(list[0])
        }
        please.await()
        return list
    }

}