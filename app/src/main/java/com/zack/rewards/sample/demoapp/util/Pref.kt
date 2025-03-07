package com.zack.rewards.sample.demoapp.util

import android.content.Context

/**
 *
 * @author zack.keng
 * Created on 2025/03/07
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class Pref private constructor(context: Context) {
    private val pref = context.getSharedPreferences("demoapp", Context.MODE_PRIVATE)

    companion object {
        lateinit var INSTANCE: Pref

        fun init(context: Context) {
            INSTANCE = Pref(context)
        }

        const val LOCK_SCREEN_TIMING_OFF = "screenOff"
        const val LOCK_SCREEN_TIMING_ON = "screenOn"
    }

    fun getLockScreenEnabled(): Boolean {
        return pref.getBoolean("lock_screen_enabled", false)
    }

    fun setLockScreenEnabled(enabled: Boolean) {
        pref.edit().putBoolean("lock_screen_enabled", enabled).apply()
    }

    fun getLockScreenTiming(): String? {
        return pref.getString("lock_screen_timing", LOCK_SCREEN_TIMING_OFF)
    }


    fun setLockScreenTiming(timing: String) {
        pref.edit().putString("lock_screen_timing", timing).apply()
    }


}