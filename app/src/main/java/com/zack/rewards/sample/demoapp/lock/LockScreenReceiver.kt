package com.zack.rewards.sample.demoapp.lock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.zack.rewards.sample.demoapp.nav.MainActivity
import com.zack.rewards.sample.demoapp.util.Pref

/**
 *
 * @author zack.keng
 * Created on 2025/03/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class LockScreenReceiver : BroadcastReceiver() {
    private lateinit var task: Handler

    companion object {
        private const val OFF_TIME = 2000L
    }

    private var off: Long = 0L

    class LockScreenRunnable(private val context: Context) : Runnable {
        override fun run() {
            Log.d("DemoApp", "LockScreenRunnable start LockScreenActivity")
            val activityIntent = Intent(context, CustomLockScreenActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(activityIntent)
        }
    }

    private fun showLockScreen(context: Context) {
        val activityIntent = Intent(context, CustomLockScreenActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        context.startActivity(activityIntent)
    }

    private var lockScreenRunnable: LockScreenRunnable? = null

    private fun startDelay(context: Context) {
        Log.d("DemoApp", "LockScreenReceiver startDelay")
        killDelay()
        val runnable = LockScreenRunnable(context)
        lockScreenRunnable = runnable
        task.postDelayed(runnable, OFF_TIME)
    }

    private fun killDelay() {
        Log.d("DemoApp", "LockScreenReceiver killDelay")
        lockScreenRunnable?.let {
            task.removeCallbacks(it)
        }

    }

    private fun getPref(context: Context): Pref {
        return try {
            Pref.INSTANCE
        } catch (e: Exception) {
            Pref.init(context)
            Pref.INSTANCE
        }
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("DemoApp", "LockScreenReceiver onReceive")
        val pref = getPref(context)
        val lockScreenEnabled = pref.getLockScreenEnabled()
        if (!lockScreenEnabled) {
            return
        }

        val timing = pref.getLockScreenTiming()
        intent?.action?.let { action ->
            task = Handler(Looper.getMainLooper())
            when (action) {
                Intent.ACTION_SCREEN_OFF -> {
                    Log.d("DemoApp", "LockScreenReceiver onReceive ACTION_SCREEN_OFF")
                    off = System.currentTimeMillis()
                    if (timing == Pref.LOCK_SCREEN_TIMING_OFF) {
                        startDelay(context)
                    } else {
                      Log.d("DemoApp", "LockScreenReceiver onReceive ACTION_SCREEN_OFF - Do nothing")
                    }
                }

                Intent.ACTION_SCREEN_ON -> {
                    if (timing == Pref.LOCK_SCREEN_TIMING_ON) {
                        Log.d("DemoApp", "LockScreenReceiver onReceive ACTION_SCREEN_ON - Show lock screen")
                        showLockScreen(context)
                    } else {
                        val on = System.currentTimeMillis()
                        Log.d(
                            "DemoApp",
                            "LockScreenReceiver onReceive ACTION_SCREEN_ON off: $off on: $on"
                        )
                        if (on - off < OFF_TIME) {
                            Log.d(
                                "DemoApp",
                                "LockScreenReceiver onReceive ACTION_SCREEN_ON - KillDelay"
                            )
                            killDelay()
                        } else {
                            Log.d(
                                "DemoApp",
                                "LockScreenReceiver onReceive ACTION_SCREEN_ON - Do nothing"
                            )
                        }
                    }

                }

                else -> Unit
            }
        }
    }
}