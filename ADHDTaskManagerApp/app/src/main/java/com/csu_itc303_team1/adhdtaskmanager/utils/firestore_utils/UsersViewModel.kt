package com.csu_itc303_team1.adhdtaskmanager.utils.firestore_utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csu_itc303_team1.adhdtaskmanager.utils.firebase.AuthUiClient
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate


class UsersViewModel(
    private val repo: UsersRepo = UsersRepo(),
) : ViewModel() {

    private val _user = MutableStateFlow<Users?>(null)
    val user: StateFlow<Users?>
        get() = _user

    private var currentUser by mutableStateOf(Users())

    private val db = FirebaseFirestore.getInstance()
    private val userCollection = db.collection("users")

    private lateinit var authUiClient: AuthUiClient

    fun checkUserInFirestore(userId: String, authUser: AuthUiClient) {
        userCollection.document(userId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("User", "Completed Searching for User in Firestore")
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("User", "Found User $userId in Firestore")
                        val user = documentSnapshot.toObject(Users::class.java)
                        val lastLogin = user?.lastLoginDate!!
                        val todaysDate = getCurrentDate()
                        if (todaysDate != lastLogin) {
                            Log.d("User", "User has not logged in today yet. Giving Log in Reward and updating last login date.")
                            updateLastLoginDate2(userId)
                            updateLoginNum2(userId)
                        } else {
                            Log.d("User", "User has already logged in today.")
                        }
                        _user.value = user
                    } else {
                        Log.d("User", "Did Not Find User in Firestore. Adding...")
                        val newUser = Users(
                            displayName = authUser.getSignedInUser()?.username,
                            points = 0,
                            emailAddress = null,
                            password = null,
                            username = authUser.getSignedInUser()?.username,
                            country = null,
                            userID = authUser.getSignedInUser()?.userId,
                            profileImage = authUser.getSignedInUser()?.profilePictureUrl,
                            loginNum = 2,
                            totalPoints = 2,
                            lastLoginDate = LocalDate.now().toString()
                        )
                        addUserToFirestore(newUser)
                    }
                } else {
                    task.exception?.printStackTrace()
                }
            }
    }

    private fun updateLastLoginDate2(userId: String) {
        val currentDate = getCurrentDate()
        userCollection.document(userId).update("lastLoginDate", currentDate)
    }

    private fun updateLoginNum2(userId: String) {
        userCollection.document(userId).update("loginNum", FieldValue.increment(2))
    }

    private fun addUserToFirestore(newUser: Users) {
        userCollection.document(newUser.userID.toString()).set(newUser)
            .addOnSuccessListener {
                _user.value = newUser
                Log.d("User", "Successfully added new User ${newUser.userID} in Firestore and set them as the current user.")
            }
            .addOnFailureListener { e ->
                Log.w("User", "There was an error user document in addToFirestore function", e)
            }
    }

    private fun getCurrentDate(): String {
        return LocalDate.now().toString()
    }

    fun initializeAuthUiClient(context: Context, oneTapClient: SignInClient) {
        authUiClient = AuthUiClient(context, oneTapClient, repo)
    }

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference


    fun fetchAndUpdateUserPoints(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                getUser(userId)  // Fetch the user. _user value will be updated after this call.

                // Wait for _user to be populated with the fetched user.
                delay(500)

                // Calculate totalPoints
                val fetchedUser = _user.value
                val totalPoints = (fetchedUser?.points ?: 0) + (fetchedUser?.loginNum ?: 0)

                // Update Firestore
                updateTotalPoints(userId, totalPoints)
            }
        }
    }

    fun loginRewardProcedure(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var storedUser = Users()
                val foundUser = repo.getUserFromFirestore(userId)

                if (foundUser != null) {
                    // Now you have the custom object stored in the 'customObject' variable
                    // You can use it as needed
                    storedUser = foundUser
                    Log.d("User", "The Run blocking code did find the user: ${storedUser.userID}")
                // ...
                } else {
                    // Handle the case where the custom object is null or an error occurred
                    Log.w("User", "The Run blocking code did not find the user.")
                }


                //delay(7000)
                //Log.d("User", "Finished Delay Time.")
                if (storedUser.userID != null) {
                    val lastLoginDate = storedUser.lastLoginDate
                    val currentDate = LocalDate.now().toString()

                    if (lastLoginDate != currentDate) {
                        updateLastLoginDate(userId, currentDate)
                        updateLogInNum(userId)
                    } else {
                        Log.d("User", "User has already claimed the log in reward today.")
                    }
                } else {
                    Log.d("User", "Stored User was null")
                }
            }
        }
    }

    private fun updateLastLoginDate(userId: String, currentDate: String){
        val db = FirebaseFirestore.getInstance().collection("users")

        db.document(userId).update("lastLoginDate", currentDate)
            .addOnSuccessListener {
                Log.d("Firestore", "Document lastLoginDate successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating lastLoginDate", e)
            }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.getUserTwo(userId).collect { user ->
                    _user.value = user
                }
            }
        }
    }

    fun updateUserCountry(userId: String, newCountry: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repo.updateCountry(userId, newCountry)
            }
        }
    }

    fun checkUserExists(id: String): Boolean {
        return repo.checkExists(id)
    }

    fun convertToUserFromAuth(authUiClient: AuthUiClient) {
        currentUser = Users(
            displayName = authUiClient.getSignedInUser()?.username,
            points = 0,
            emailAddress = null,
            password = null,
            username = authUiClient.getSignedInUser()?.username,
            country = null,
            userID = authUiClient.getSignedInUser()?.userId,
            profileImage = authUiClient.getSignedInUser()?.profilePictureUrl,
            loginNum = 2,
            totalPoints = 2,
            lastLoginDate = LocalDate.now().toString()
        )
    }



    fun updateTotalPoints(userId: String, totalPoints: Int) {
        // Reference to the Firestore collection.
        val db = FirebaseFirestore.getInstance().collection("users")

        db.document(userId).update("totalPoints", totalPoints)
            .addOnSuccessListener {
                Log.d("Firestore", "Document successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating document", e)
            }
    }

    fun updateLogInNum(userId: String) {
        val db = FirebaseFirestore.getInstance().collection("users")

        db.document(userId).update("loginNum", FieldValue.increment(2))
            .addOnSuccessListener {
                Log.d("Firestore", "Updated LoginNum successfully !")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating loginNum", e)
            }
    }


    fun addUserToFirebase() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addToFirebaseDatabase(currentUser)
            _user.value = currentUser
        }
    }

    fun completedTaskPoints() {
        val points = user.value?.points?.plus(3)
        if (points != null) {
            user.value?.let { repo.updatePoints(it, points) }
        }
    }

    fun fetchImageList(directory: String): Flow<List<Uri>> = flow {
        val listResult = storageRef.child(directory).listAll().await()
        val uriList = mutableListOf<Uri>()
        for (reference in listResult.items) {
            val downloadUri = reference.getDownloadUrl().await() // Corrected here
            uriList.add(downloadUri)
        }
        emit(uriList)
    }

}