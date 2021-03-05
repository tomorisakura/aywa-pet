package com.grevi.aywapet.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkUtils(context: Context) : ConnectivityManager.NetworkCallback() {
    private val appContext = context
    private val _networkStatus = MutableLiveData<Boolean>()
    val networkStatus : MutableLiveData<Boolean> get() = _networkStatus

    init {
        observeNetworkState()
    }

    private fun observeNetworkState() : LiveData<Boolean> {
        val connectivityManager = appContext.getSystemService<ConnectivityManager>()
        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(this)
            val connectionStatus = connectivityManager.allNetworks.any { network ->
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                return@any networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
            }
            _networkStatus.postValue(connectionStatus)
        }
        return _networkStatus
    }

    override fun onAvailable(network: Network) {
        _networkStatus.postValue(true)
    }

    override fun onLost(network: Network) {
        _networkStatus.postValue(false)
    }
}