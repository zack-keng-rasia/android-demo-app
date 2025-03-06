package com.zack.rewards.sample.demoapp.lock

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.zack.rewards.sample.demoapp.databinding.ActivityLockScreenBinding
import kotlin.math.abs

/**
 *
 * @author zack.keng
 * Created on 2025/03/04
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class CustomLockScreenActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("DemoApp", "CustomLockScreenActivity onCreate")
        super.onCreate(savedInstanceState)
        val binding = ActivityLockScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }

        gestureDetector = GestureDetector(this, SwipeGestureListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let { gestureDetector.onTouchEvent(it) } == true || super.onTouchEvent(event)
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1 != null) {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffY) > abs(diffX)) {
                    if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD && diffY < 0) {
                        // Swipe up detected
                        finish()
                        return true
                    }
                } else {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD && diffX > 0) {
                        // Swipe right detected
                        finish()
                        return true
                    }
                }
            }
            return false
        }
    }
}