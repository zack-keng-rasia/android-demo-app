package com.zack.rewards.sample.demoapp.leak

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zack.rewards.sample.demoapp.R

class MemoryLeakActivity : AppCompatActivity(), NetworkListener {
    private lateinit var broadcastReceiver: NetworkBroadcastReceiver
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_leak)

        textView = findViewById(R.id.network_status)

        broadcastReceiver = NetworkBroadcastReceiver()

//        (applicationContext as MyApp).addView(textView)
        LeakObject.setListener(this)
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
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