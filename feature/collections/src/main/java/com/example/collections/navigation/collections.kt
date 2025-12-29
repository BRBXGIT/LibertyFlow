package com.example.collections.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.collections.screen.Collections
import com.example.collections.screen.CollectionsVM
import com.example.common.navigation.CollectionsRoute
import com.example.common.ui_helpers.HandleCommonEffects
import com.example.design_system.utils.standardScreenEnterTransition
import com.example.design_system.utils.standardScreenExitTransition

fun NavGraphBuilder.collections(
    collectionsVM: CollectionsVM,
    navController: NavController
) = composable<CollectionsRoute>(
    enterTransition = { standardScreenEnterTransition() },
    exitTransition = { standardScreenExitTransition() }
) {
    val collectionsState by collectionsVM.collectionsState.collectAsStateWithLifecycle()
    val collectionEffects = collectionsVM.collectionEffects

    val plannedAnime = collectionsVM.plannedAnime.collectAsLazyPagingItems()
    val watchedAnime = collectionsVM.watchedAnime.collectAsLazyPagingItems()
    val watchingAnime = collectionsVM.watchingAnime.collectAsLazyPagingItems()
    val postponedAnime = collectionsVM.postponedAnime.collectAsLazyPagingItems()
    val abandonedAnime = collectionsVM.abandonedAnime.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    HandleCommonEffects(
        effects = collectionEffects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Collections(
        collectionsState = collectionsState,
        collections = listOf(plannedAnime, watchedAnime, watchingAnime, postponedAnime, abandonedAnime),
        snackbarHostState = snackbarHostState,
        onIntent = collectionsVM::sendIntent,
        onEffect = collectionsVM::sendEffect
    )
}