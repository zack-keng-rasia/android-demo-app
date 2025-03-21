package com.zack.rewards.sample.demoapp.util

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class UrlBuilder(private val baseUrl: String) {
    private val query: MutableMap<String, String> = mutableMapOf()
    private var path: String = ""

    fun addQuery(key: String, value: String): UrlBuilder {
        query[key] = value
        return this
    }

    fun addPath(path: String): UrlBuilder {
        this.path = path
        return this
    }

    fun build(): String {
        val url = StringBuilder(baseUrl)
        if (path.isNotEmpty()) {
            url.append("/").append(path)
        }
        if (query.isNotEmpty()) {
            url.append("?")
            query.forEach { (key, value) ->
                url.append(key).append("=").append(value).append("&")
            }
            url.deleteCharAt(url.length - 1)
        }
        return url.toString()
    }

}