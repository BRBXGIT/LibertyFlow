package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.example.collections.screen.CollectionsIntent
import com.example.data.models.common.mappers.toCollection
import com.example.data.models.common.mappers.toIndex
import com.example.data.models.common.request.request_parameters.Collection

@Composable
fun CollectionsPager(
    state: PagerState,
    currentCollection: Collection,
    onIntent: (CollectionsIntent) -> Unit,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    LaunchedEffect(currentCollection) {
        state.scrollToPage(currentCollection.toIndex())
    }

    LaunchedEffect(state.currentPage) {
        onIntent(CollectionsIntent.SetCollection(state.currentPage.toCollection()))
    }

    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
        key = { it },
    ) { page ->
        key(page) {
            pageContent(page)
        }
    }
}