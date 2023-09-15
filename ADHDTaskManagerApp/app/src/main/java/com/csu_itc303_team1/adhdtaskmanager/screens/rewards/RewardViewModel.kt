package com.csu_itc303_team1.adhdtaskmanager.screens.rewards

import androidx.lifecycle.ViewModel
import com.csu_itc303_team1.adhdtaskmanager.model.FirestoreUser
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService
import com.csu_itc303_team1.adhdtaskmanager.model.service.UsersStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RewardViewModel @Inject constructor(
    private val usersStorageService: UsersStorageService,
    private val connectivityObserverService: ConnectivityObserverService
): ViewModel() {

    private var _user = usersStorageService.currentUser
    val connectivityObserver = connectivityObserverService.observeConnectivity()

    fun getUser(): Flow<FirestoreUser?> {
        return _user
    }
}