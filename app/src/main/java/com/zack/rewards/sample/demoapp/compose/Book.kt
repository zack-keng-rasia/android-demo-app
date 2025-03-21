package com.zack.rewards.sample.demoapp.compose

import org.json.JSONObject

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
data class Book(
    val id: String = "121",
    val title: String = "121121",
    val publishedDate: String = "2020-01-01"
) {
    companion object {
        fun fromJsonObject(json: JSONObject): Book {
            val id = json.getString("id")
            val title = json.getJSONObject("volumeInfo").getStringOrDefault("title", "Unknown")
            val publishedDate = json.getJSONObject("volumeInfo").getStringOrDefault("publishedDate", "xxxx-xx-xx")
            return Book(id, title, publishedDate)
        }
    }
}

fun JSONObject.getStringOrDefault(key: String, default: String): String {
    return if (this.has(key)) {
        this.getString(key)
    } else {
        default
    }
}
