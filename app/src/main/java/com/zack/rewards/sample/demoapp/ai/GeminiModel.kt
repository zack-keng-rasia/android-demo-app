package com.zack.rewards.sample.demoapp.ai

/**
 *
 * @author zack.keng
 * Created on 2024/10/17
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
enum class GeminiModel(val modelName: String) {
    GEMINI_2_0_FLASH("gemini-2.0-flash-001"),
    GEMINI_2_5_PRO_PREVIEW("gemini-2.5-pro-preview-05-06"),
    IMAGEN_3_0("imagen-3.0-generate-002"),

    // DEPRECATED MODELS
    /*GEMINI_1_5_FLASH("gemini-1.5-flash"),
    GEMINI_1_0_PRO("gemini-1.0-pro"),
    GEMINI_1_0_PRO_VISION("gemini-1.0-pro-vision"),
    GEMINI_1_5_PRO("gemini-1.5-pro")*/

}