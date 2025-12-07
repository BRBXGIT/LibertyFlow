package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.models.common.request.request_parameters.Collection

@Composable
fun CollectionsPager(
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    val state = rememberPagerState { Collection.entries.size }

    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
        pageContent = pageContent
    )
}