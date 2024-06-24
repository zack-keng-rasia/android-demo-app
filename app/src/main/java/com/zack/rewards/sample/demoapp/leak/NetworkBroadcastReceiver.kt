package com.zack.rewards.sample.demoapp.leak

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkBroadcastReceiver: BroadcastReceiver() {
    enum class NetworkStatus {
        CONNECTED,
        DISCONNECTED
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            // Perform action based on network connection status
            if (isConnected) {
                // Network is connected
                LeakObject.getListener()?.onNetworkChanged(NetworkStatus.CONNECTED)
            } else {
                // Network is disconnected
                LeakObject.getListener()?.onNetworkChanged(NetworkStatus.DISCONNECTED)
            }
        }
    }
}