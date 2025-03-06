package com.zack.rewards.sample.demoapp.lock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 *
 * @author zack.keng
 * Created on 2025/03/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class LockScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("DemoApp", "LockScreenReceiver onReceive")
        intent?.action?.let {
            if (Intent.ACTION_SCREEN_ON == it || Intent.ACTION_USER_PRESENT == it) {
                val serviceIntent = Intent(context, LockScreenService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.d("DemoApp", "LockScreenReceiver startForegroundService")
                    context?.startForegroundService(serviceIntent)
                } else {
                    context?.startService(serviceIntent)
//                    val activityIntent =
//                        Intent(context, CustomLockScreenActivity::class.java)
//                    activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context?.startActivity(activityIntent)
                }

            }
        }
    }
}