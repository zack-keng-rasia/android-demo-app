package com.zack.rewards.sample.demoapp.leak

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zack.rewards.sample.demoapp.MyApp
import com.zack.rewards.sample.demoapp.R

class MemoryLeakActivity : AppCompatActivity(), NetworkListener {
    private lateinit var broadcastReceiver: NetworkBroadcastReceiver
    private lateinit var textView: TextView

    private val networkListener by lazy {
        object: NetworkListener {
            override fun onNetworkChanged(status: NetworkBroadcastReceiver.NetworkStatus) {
                networkChanged(status)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_leak)
        textView = findViewById(R.id.network_status)

        // ======== 1 Initialize Broadcast Receiver =======
        broadcastReceiver = NetworkBroadcastReceiver(networkListener)
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        // ========= 2 Add View to Application ========
        (applicationContext as MyApp).addView(textView)

        // ======== 3 Set Listener to LeakObject =======
//        LeakObject.setListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        // ======== 1 Unregister Broadcast Receiver =======
        unregisterReceiver(broadcastReceiver)
    }

    private fun networkChanged(status: NetworkBroadcastReceiver.NetworkStatus) {
        textView.text = status.toString()

        when (status) {
            NetworkBroadcastReceiver.NetworkStatus.CONNECTED -> {
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
            }

            NetworkBroadcastReceiver.NetworkStatus.DISCONNECTED -> {
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            }
        }
    }

    override fun onNetworkChanged(status: NetworkBroadcastReceiver.NetworkStatus) {
        networkChanged(status)
    }
}