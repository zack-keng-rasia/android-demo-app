package com.zack.rewards.sample.demoapp.compose

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class BookPagingSource(
    private val query: String,
    private val repository: BookRepository
): PagingSource<Int, Book>() {
    override fun getRefreshKey(state: PagingState<Int, Book>): Int {
        val refreshKey = ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2)
            .coerceAtLeast(0)
        Log.d("BookPagingSource", "getRefreshKey: $refreshKey")
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        Log.d("BookPagingSource", "load: ${params.key}, ${params.loadSize}")
        if (query.isBlank()) {
            return LoadResult.Page(emptyList(), prevKey = null, nextKey = null)
        }
        return try {
            val nextPageNumber = params.key ?: 1
            val response = repository.getBooks(query, nextPageNumber, params.loadSize)

            LoadResult.Page(
                data = response.items,
                prevKey = params.prevKey(),
                nextKey = params.nextKey(response.totalItems)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

fun PagingSource.LoadParams<Int>.prevKey() =
    (key?.coerceAtLeast(0) ?: 0).takeIf { it > 0 }?.minus(loadSize)?.coerceAtLeast(0)

fun PagingSource.LoadParams<Int>.nextKey(total: Int) =
    (key?.coerceAtLeast(0) ?: 0).plus(loadSize).takeIf { it <= total }