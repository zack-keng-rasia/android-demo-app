package com.zack.rewards.sample.demoapp.ai

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import com.zack.rewards.sample.demoapp.BuildConfig

/**
 *
 * @author zack.keng
 * Created on 2024/10/17
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
object GoogleGenAI {

    fun newModel(
        geminiModel: GeminiModel,
        providedTemp: Float,
        providedTopK: Int,
        providedTopP: Float,
        providedMaxT: Int
    ): GenerativeModel {
        return GenerativeModel(
            modelName = geminiModel.modelName,
            apiKey = BuildConfig.googleAiApiKey,
            generationConfig = generationConfig {
                temperature = providedTemp
                topK = providedTopK
                topP = providedTopP
                maxOutputTokens = providedMaxT
            },
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
            )
        )
    }
}