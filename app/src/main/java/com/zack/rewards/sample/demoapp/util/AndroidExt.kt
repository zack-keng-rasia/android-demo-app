package com.zack.rewards.sample.demoapp.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zack.rewards.sample.demoapp.lock.LockScreenService

/**
 *
 * @author zack.keng
 * Created on 2023/08/14
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showLongToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Context.toastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hideKeyboard(view: View) {
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        view.windowToken,
        0
    )
}

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Context.restartLockScreenService(tag: String) {
    val stopLockScreen: () -> Unit = {
        if (isServiceRunning(LockScreenService::class.java)) {
            Log.d("DemoApp", "$tag stopService")
            stopService(Intent(this, LockScreenService::class.java))
        } else {
            Log.d("DemoApp", "$tag service not running")
        }
    }

    val startLockScreen: () -> Unit = {
        if (isServiceRunning(LockScreenService::class.java)) {
            Log.d("DemoApp", "$tag service already running")
        } else {
            Log.d("DemoApp", "$tag StartForegroundService")
            ContextCompat.startForegroundService(this, Intent(this, LockScreenService::class.java))
        }
    }

    stopLockScreen()
    startLockScreen()
}