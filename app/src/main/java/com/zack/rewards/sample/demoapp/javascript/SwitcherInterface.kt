package com.zack.rewards.sample.demoapp.javascript

import android.webkit.JavascriptInterface

/**
 *
 * @author zack.keng
 * Created on 2023/08/15
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class SwitcherInterface(private val callback: (Boolean) -> Unit) {

    @JavascriptInterface
    fun changeSwitch(to: String) {
        if (to.equals("ON", true)) {
            callback(true)
        } else {
            callback(false)
        }
    }
}