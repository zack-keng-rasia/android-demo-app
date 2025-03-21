package com.zack.rewards.sample.demoapp.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 *
 * @author zack.keng
 * Created on 2025/03/19
 * Copyright © 2025 Rakuten Asia. All rights reserved.
 */
@Composable
fun BookItem(
    book: Book,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            Modifier
                .fillMaxHeight()
                .weight(0.8f)
                .padding(30.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = book.publishedDate,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookItemPreview() {
    BookItem(
        book =
        Book(),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    )
}