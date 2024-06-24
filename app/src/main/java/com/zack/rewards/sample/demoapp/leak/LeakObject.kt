package com.zack.rewards.sample.demoapp.leak

object LeakObject {
    private var listener: NetworkListener? = null

    fun setListener(listener: NetworkListener?) {
        this.listener = listener
    }

    fun getListener(): NetworkListener? {
        return listener
    }
}