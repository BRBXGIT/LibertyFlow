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

/**
 * Extension function for [NavGraphBuilder] to define the Collections screen destination.
 * * **Navigation:** Uses [CollectionsRoute] with custom fade transitions.
 * * **State Management:** Collects state from multiple ViewModels ([CollectionsVM], [RefreshVM], [ThemeVM]).
 * * **Paging Lifecycle:** Converts Paging Flows into [LazyPagingItems] scoped to this Composable.
 *
 * @param collectionsVM Primary ViewModel for UI state and paging flows.
 * @param refreshVM ViewModel used to trigger cross-screen data refreshes.
 * @param themeVM ViewModel providing the current visual theme configuration.
 * @param navController Controller for handling navigation actions (e.g., clicking an anime item).
 */
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

    /**
     * UI Logic: Collect all flows into a Map of LazyPagingItems.
     * This conversion must happen within the Composable scope to properly
     * manage the PagingData lifecycle.
     */
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

/**
 * A side-effect handler that listens for [RefreshEffect] signals.
 * * When a refresh is requested for a specific [Collection], it calls [LazyPagingItems.refresh]
 * on the corresponding entry in the map.
 *
 * @param effects A [Flow] of refresh events usually coming from a shared [RefreshVM].
 * @param collections The map of active paging items to be refreshed.
 */
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