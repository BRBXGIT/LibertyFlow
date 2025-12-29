package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.example.collections.screen.CollectionsIntent
import com.example.data.models.common.mappers.toCollection

@Composable
fun CollectionsPager(
    state: PagerState,
    onIntent: (CollectionsIntent) -> Unit,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    LaunchedEffect(state) {
        snapshotFlow { state.currentPage }.collect { page ->
            onIntent(CollectionsIntent.SetCollection(page.toCollection()))
        }
    }

    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
        key = { it }
    ) { page ->
        pageContent(page)
    }
}