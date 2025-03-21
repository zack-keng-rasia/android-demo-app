package com.zack.rewards.sample.demoapp.compose

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.zack.rewards.sample.demoapp.util.UrlBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class BookRepository(
    context: Context,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val requestQueue = Volley.newRequestQueue(context)

    suspend fun getBooks(query: String, startIndex: Int, maxResults: Int): BookResponse {
        Log.d("BookRepository", "getBooks: $query, $startIndex, $maxResults")
        return withContext(defaultDispatcher) {
            suspendCoroutine { suspend ->
                val url = UrlBuilder("https://www.googleapis.com").apply {
                    addPath("books/v1/volumes")
                    addQuery("q", query)
                    addQuery("startIndex", "$startIndex")
                    addQuery("maxResults", "$maxResults")
                }.build()
                Log.d("BookRepository", "getBooks: $url")
                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    { response ->
                        val books = mutableListOf<Book>()
                        if (response.has("items")) {
                            val jsonArray = response.getJSONArray("items")
                            for (i in 0 until jsonArray.length()) {
                                val book = Book.fromJsonObject(jsonArray.getJSONObject(i))
                                books.add(book)
                            }
                        }
                        val totalItems = response.getInt("totalItems")
                        Log.d("BookRepository", "Book Response: $totalItems, ${books.size}")
                        val bookResponse = BookResponse(totalItems, books)
                        suspend.resume(bookResponse)
                    },
                    { error ->
                        suspend.resumeWithException(error)
                    }
                )
                requestQueue.add(jsonObjectRequest)

            }
        }

    }
}

data class BookResponse(
    val totalItems: Int,
    val items: List<Book>
)