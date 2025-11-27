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
    val collectionAnime = collectionsVM.collectionAnime.collectAsLazyPagingItems()

    Collections(
        collectionsState = collectionsState,
        collectionAnime = collectionAnime,
        onIntent = collectionsVM::sendIntent,
    )
}