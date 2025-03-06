package com.zack.rewards.sample.demoapp.lock

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

/**
 *
 * @author zack.keng
 * Created on 2025/03/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class LockScreenService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DemoApp", "LockScreenService onStartCommand")
        val notification = NotificationCompat.Builder(this, "LockScreenServiceChannel")
            .setContentTitle("Lock Screen")
            .setContentText("Lock Screen Service")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .build()
        startForeground(1, notification)

        val activityIntent = Intent(this, CustomLockScreenActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(activityIntent)

        stopSelf()
        return START_NOT_STICKY
    }
}