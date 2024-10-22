package com.zack.rewards.sample.demoapp.ai

import android.graphics.Bitmap

/**
 *
 * @author zack.keng
 * Created on 2024/10/18
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
data class MessageItem(
    val message: String,
    val role: Role,
    val bitmap: Bitmap? = null
)

enum class Role(val role: String) {
    USER("user"), MODEL("model")
}
