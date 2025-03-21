package com.zack.rewards.sample.demoapp.compose

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.zack.rewards.sample.demoapp.MyApp

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class BookViewModel: ViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
    }

    var query = mutableStateOf("")
        private set

    private val repository = BookRepository(MyApp.instance.applicationContext)
    private lateinit var pagingSource : BookPagingSource

    val bookPager = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        BookPagingSource(query.value, repository).also { pagingSource = it }
    }.flow

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun invalidateDataSource() {
        pagingSource.invalidate()
    }

}