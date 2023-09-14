package com.csu_itc303_team1.adhdtaskmanager.model.service.impl

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService
import kotlinx.coroutines.flow.Flow
import com.csu_itc303_team1.adhdtaskmanager.model.service.ConnectivityObserverService.Status
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConnectivityObserverServiceImpl @Inject constructor(
    private val context: Context
): ConnectivityObserverService {
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ServiceCast")
    override fun observeConnectivity(): Flow<Status> {
        return callbackFlow {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    launch {
                        send(Status.CONNECTED)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(Status.DISCONNECTED)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(Status.LOST)
                    }
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                    super.onLosing(network, maxMsToLive)
                    launch {
                        send(Status.LOSING)
                    }
                }
            }
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
            awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
        }.distinctUntilChanged()
    }
}