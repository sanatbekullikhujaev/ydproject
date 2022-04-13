package com.usc0der.ydprojectnew.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkLiveData constructor(
    private val context: Context
) : LiveData<Boolean>() {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    init {
        val connectManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) connectManager.registerDefaultNetworkCallback(
            networkCallback
        )
        else {
            val builder = NetworkRequest.Builder()
            connectManager.registerNetworkCallback(builder.build(), networkCallback)
        }

        var isConnected = false

        connectManager.allNetworks.forEach { network ->
            connectManager.getNetworkCapabilities(network)?.let { capabilities ->
                isConnected =
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                if (isConnected) return@forEach
            }
        }

        postValue(isConnected)
    }

    override fun onInactive() {
//        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager?)?.apply {
//            unregisterNetworkCallback(networkCallback)
//        }
        super.onInactive()
    }
}