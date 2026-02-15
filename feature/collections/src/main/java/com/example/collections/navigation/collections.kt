package com.example.collections.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.common.refresh.RefreshEffect
import com.example.common.refresh.RefreshVM
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.utils.fadeScreenEnterTransition
import com.example.design_system.utils.fadeScreenExitTransition
import com.example.data.models.common.request.request_parameters.Collection
import com.example.design_system.theme.logic.ThemeVM
import kotlinx.coroutines.flow.Flow

fun NavGraphBuilder.collections(
    collectionsVM: CollectionsVM,
    refreshVM: RefreshVM,
    themeVM: ThemeVM,
    navController: NavController
) = composable<CollectionsRoute>(
    enterTransition = { fadeScreenEnterTransition() },
    exitTransition = { fadeScreenExitTransition() }
) {
    val themeState by themeVM.themeState.collectAsStateWithLifecycle()
    val state by collectionsVM.state.collectAsStateWithLifecycle()

    // UI Logic: Collect all flows into a Map of LazyPagingItems.
    // This must be done in the Composable scope.
    // The key is the Collection Enum, ensuring O(1) access in the Pager.
    val pagingItemsMap: Map<Collection, LazyPagingItems<AnimeItem>> =
        Collection.entries.associateWith { collection ->
            collectionsVM.pagingFlows.getValue(collection).collectAsLazyPagingItems()
        }

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = collectionsVM.effects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    // Handle collections that needs to be refreshed
    RefreshCollection(refreshVM.refreshEffects, pagingItemsMap)

    Collections(
        state = state,
        themeState = themeState,
        pagingItemsMap = pagingItemsMap,
        snackbarHostState = snackbarHostState,
        onIntent = collectionsVM::sendIntent,
        onEffect = collectionsVM::sendEffect
    )
}

@Composable
private fun RefreshCollection(
    effects: Flow<RefreshEffect>,
    collections: Map<Collection, LazyPagingItems<AnimeItem>>
) {
    LaunchedEffect(Unit) {
        effects.collect { effect ->
            when(effect) {
                is RefreshEffect.RefreshCollection -> if (effect.collection != null) collections[effect.collection]?.refresh()
                else -> {}
            }
        }
    }
}