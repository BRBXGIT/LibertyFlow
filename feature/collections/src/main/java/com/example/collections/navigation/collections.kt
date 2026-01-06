package com.example.collections.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.collections.screen.Collections
import com.example.collections.screen.CollectionsVM
import com.example.common.navigation.CollectionsRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition
import com.example.data.models.common.request.request_parameters.Collection

fun NavGraphBuilder.collections(
    collectionsVM: CollectionsVM,
    navController: NavController
) = composable<CollectionsRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
) {
    val state by collectionsVM.state.collectAsStateWithLifecycle()

    // UI Logic: Collect all flows into a Map of LazyPagingItems.
    // This must be done in the Composable scope.
    // The key is the Collection Enum, ensuring O(1) access in the Pager.
    val pagingItemsMap: Map<Collection, LazyPagingItems<UiAnimeItem>> =
        Collection.entries.associateWith { collection ->
            collectionsVM.pagingFlows.getValue(collection).collectAsLazyPagingItems()
        }

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = collectionsVM.effects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Collections(
        state = state,
        pagingItemsMap = pagingItemsMap,
        snackbarHostState = snackbarHostState,
        onIntent = collectionsVM::sendIntent,
        onEffect = collectionsVM::sendEffect
    )
}