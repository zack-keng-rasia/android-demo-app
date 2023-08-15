package com.zack.rewards.sample.demoapp.javascript

import android.content.Context
import android.webkit.JavascriptInterface
import com.zack.rewards.sample.demoapp.util.toastMessage

/**
 *
 * @author zack.keng
 * Created on 2023/08/15
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
class WebAppInterface(private val mContext: Context) {

    @JavascriptInterface
    fun showToast(toast: String) {
        mContext.toastMessage(toast)
    }
}