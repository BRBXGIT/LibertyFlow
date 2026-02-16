package com.example.home.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.common.navigation.HomeRoute
import com.example.common.ui_helpers.effects.HandleCommonEffects
import com.example.design_system.utils.fadeScreenEnterTransition
import com.example.design_system.utils.fadeScreenExitTransition
import com.example.home.screen.Home
import com.example.home.screen.HomeVM

/**
 * Adds the Home screen destination to the [NavGraphBuilder].
 *
 * This function serves as the entry point for the Home feature, orchestrating the
 * connection between the ViewModel and the UI. It handles state collection,
 * paging data, transitions, and side effects.
 *
 * ### Key Responsibilities:
 * - **State Management:** Collects HomeState using [collectAsStateWithLifecycle]
 * for lifecycle-aware UI updates.
 * - **Pagination:** Manages the LazyPagingItems for the anime list, ensuring
 * efficient data loading.
 * - **Transitions:** Applies custom fade-in and fade-out animations for screen
 * entry and exit.
 * - **Effect Handling:** Utilizes [HandleCommonEffects] to process one-time
 * events like navigation or showing [SnackbarHostState] messages.
 *
 * @param homeVM The [HomeVM] instance providing state and handling user intents.
 * @param navController The [NavController] used for navigating to other screens
 * based on ViewModel effects.
 */
fun NavGraphBuilder.home(
    homeVM: HomeVM,
    navController: NavController
) = composable<HomeRoute>(
    enterTransition = { fadeScreenEnterTransition() },
    exitTransition = { fadeScreenExitTransition() }
) {
    val state by homeVM.state.collectAsStateWithLifecycle()
    val anime = homeVM.anime.collectAsLazyPagingItems()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation & snackbar effects
    HandleCommonEffects(
        effects = homeVM.effects,
        navController = navController,
        snackbarHostState = snackbarHostState
    )

    Home(
        state = state,
        anime = anime,
        snackbarHostState = snackbarHostState,
        onIntent = homeVM::sendIntent,
        onEffect = homeVM::sendEffect
    )
}