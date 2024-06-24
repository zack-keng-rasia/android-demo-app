package com.zack.rewards.sample.demoapp.leak

interface NetworkListener {

    fun onNetworkChanged(status: NetworkBroadcastReceiver.NetworkStatus)
}