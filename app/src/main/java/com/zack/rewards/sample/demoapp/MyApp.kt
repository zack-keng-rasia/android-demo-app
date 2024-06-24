package com.zack.rewards.sample.demoapp

import android.app.Application
import android.view.View

class MyApp: Application() {
    private val cacheViews = mutableListOf<View>()

    fun addView(view: View) {
        cacheViews.add(view)
    }
}