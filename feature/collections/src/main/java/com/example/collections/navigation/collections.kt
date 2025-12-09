package com.example.collections.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.collections.screen.Collections
import com.example.collections.screen.CollectionsVM
import com.example.common.navigation.CollectionsRoute

fun NavGraphBuilder.collections(
    collectionsVM: CollectionsVM
) = composable<CollectionsRoute> {
    val collectionsState by collectionsVM.collectionsState.collectAsStateWithLifecycle()

    val plannedAnime = collectionsVM.plannedAnime.collectAsLazyPagingItems()
    val watchedAnime = collectionsVM.watchedAnime.collectAsLazyPagingItems()
    val watchingAnime = collectionsVM.watchingAnime.collectAsLazyPagingItems()
    val postponedAnime = collectionsVM.postponedAnime.collectAsLazyPagingItems()
    val abandonedAnime = collectionsVM.abandonedAnime.collectAsLazyPagingItems()

    Collections(
        collectionsState = collectionsState,
        collections = listOf(plannedAnime, watchedAnime, watchingAnime, postponedAnime, abandonedAnime),
        onIntent = collectionsVM::sendIntent,
    )
}