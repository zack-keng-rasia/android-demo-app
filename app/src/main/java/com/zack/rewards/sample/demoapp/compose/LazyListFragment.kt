package com.zack.rewards.sample.demoapp.compose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.zack.rewards.sample.demoapp.nav.InfoFragment
import kotlinx.coroutines.flow.flowOf

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
class LazyListFragment : InfoFragment() {
    private val viewModel: BookViewModel by viewModels()

    override fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun BookList(books: LazyPagingItems<Book>, modifier: Modifier = Modifier) {
        when (books.loadState.refresh) {
            LoadState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Something went wrong")
                }
            }

            else -> {
                LazyColumn(modifier = modifier) {
                    items(
                        books.itemCount
                    ) { index ->
                        books[index]?.let {
                            BookItem(
                                book = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(getBackgroundForIndex(index))
                                    .padding(vertical = 15.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val books = viewModel.bookPager.collectAsLazyPagingItems()
        val focusManager = LocalFocusManager.current
        Column(modifier = Modifier.fillMaxSize()) {
            SearchView(
                query = viewModel.query.value,
                onQueryChanged = { newQuery ->
                    viewModel.setQuery(newQuery)
                },
                onSearch = {
                    viewModel.invalidateDataSource()
                    focusManager.clearFocus()
                },
                onClearQuery = {
                    viewModel.setQuery("")
                    viewModel.invalidateDataSource()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    )
            )
            BookList(books)
        }
    }

    fun getBackgroundForIndex(index: Int) =
        if (index % 2 == 0) LightGray
        else Color.White

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BookList(
            flowOf(
                PagingData.from(
                    listOf(
                        Book()
                    )
                )
            ).collectAsLazyPagingItems()
        )
    }

    override fun infoIconClicked() {
        showInfoDialog("This page is to demo lazy load with pagination in Compose")
    }
}