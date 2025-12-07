package com.example.collections.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.collections.screen.CollectionsIntent
import com.example.data.models.common.mappers.toCollection
import com.example.data.models.common.mappers.toPage
import com.example.data.models.common.request.request_parameters.Collection

@Composable
fun CollectionsPager(
    currentCollection: Collection,
    onIntent: (CollectionsIntent) -> Unit,
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    val state = rememberPagerState { Collection.entries.size }

    LaunchedEffect(currentCollection) {
        state.animateScrollToPage(currentCollection.toPage())
    }

    LaunchedEffect(state.currentPage) {
        onIntent(CollectionsIntent.SetCollection(state.currentPage.toCollection()))
    }

    HorizontalPager(
        state = state,
        modifier = Modifier.fillMaxSize(),
        pageContent = pageContent
    )
}