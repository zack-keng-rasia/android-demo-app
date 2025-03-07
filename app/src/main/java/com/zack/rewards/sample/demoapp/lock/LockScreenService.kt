package com.zack.rewards.sample.demoapp.lock

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zack.rewards.sample.demoapp.nav.MainActivity

/**
 *
 * @author zack.keng
 * Created on 2025/03/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class LockScreenService : Service() {

    inner class LockScreenServiceBinder: Binder() {
        fun getService(): LockScreenService = this@LockScreenService
    }

    private val mBinder = LockScreenServiceBinder()
    private val notificationManager by lazy { NotificationManagerCompat.from(this) }
    private val notification: Notification by lazy { makeNotification() }
    private var lockScreenReceiver: LockScreenReceiver? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        Log.d("DemoApp", "LockScreenService onCreate")
        super.onCreate()

        createNotificationChannel()
        notificationManager.notify(111, notification)
        startForeground(111, notification)
        startReceiver()
    }

    override fun onDestroy() {
        Log.d("DemoApp", "LockScreenService onDestroy")
        super.onDestroy()
        try {
            if (lockScreenReceiver != null) {
                unregisterReceiver(lockScreenReceiver)
                lockScreenReceiver = null
            }
        } catch (e: Exception) {
            Log.e("DemoApp", "LockScreenService onDestroy error: ${e.message}")
        }
    }

    private fun startReceiver() {
        Log.d("DemoApp", "LockScreenService startReceiver")
        if (lockScreenReceiver != null) {
            unregisterReceiver(lockScreenReceiver)
        }

        lockScreenReceiver = LockScreenReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_SCREEN_ON)
        registerReceiver(lockScreenReceiver, filter)
    }

    private fun makeNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 111, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "LockScreenServiceChannel")
            .setContentTitle("Lock Screen")
            .setContentText("Lock Screen Service")
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .build()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "LockScreenServiceChannel",
                "Lock Screen",
                NotificationManager.IMPORTANCE_MIN
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder = mBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DemoApp", "LockScreenService onStartCommand")
        startForeground(111, notification)

        return START_STICKY
    }
}