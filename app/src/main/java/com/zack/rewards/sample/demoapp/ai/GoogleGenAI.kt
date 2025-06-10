package com.zack.rewards.sample.demoapp.ai

import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ImagenModel
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.HarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory
import com.google.firebase.ai.type.PublicPreviewAPI
import com.google.firebase.ai.type.SafetySetting
import com.google.firebase.ai.type.generationConfig

/**
 *
 * @author zack.keng
 * Created on 2024/10/17
 * Copyright © 2024 Rakuten Asia. All rights reserved.
 */
@OptIn(PublicPreviewAPI::class)
object GoogleGenAI {

    fun imagenModel(): ImagenModel {
        return FirebaseAI.getInstance(backend = GenerativeBackend.googleAI())
            .imagenModel(modelName = GeminiModel.IMAGEN_3_0.modelName)
    }

    fun newModel(
        geminiModel: GeminiModel,
        providedTemp: Float,
        providedTopK: Int,
        providedTopP: Float,
        providedMaxT: Int
    ): GenerativeModel {
        return FirebaseAI.getInstance(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = geminiModel.modelName,
                generationConfig = generationConfig {
                    temperature = providedTemp
                    topK = providedTopK
                    topP = providedTopP
                    maxOutputTokens = providedMaxT
                },
                safetySettings = listOf(
                    SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(
                        HarmCategory.SEXUALLY_EXPLICIT,
                        HarmBlockThreshold.MEDIUM_AND_ABOVE
                    ),
                    SafetySetting(
                        HarmCategory.DANGEROUS_CONTENT,
                        HarmBlockThreshold.MEDIUM_AND_ABOVE
                    )
                )
            )
    }
}