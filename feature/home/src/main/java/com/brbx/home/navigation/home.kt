package com.brbx.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.composable.screen.LibertyFlowScreen
import com.brbx.common.composable.screen.PagingHandler
import com.brbx.home.composable.HomeScreenScaffold
import com.brbx.home.composable.content.filters_sheet.HomeFiltersSheet
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.home.view_model.view_model.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.home(
    navController: NavController,
) = composable<HomeRoute> {
    val viewModel = koinViewModel<HomeViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val catalog = state.catalog.pagingData.collectAsLazyPagingItems()

    if (state.filtersSheet.isVisible) {
        HomeFiltersSheet(
            filters = state.filtersSheet,
            dispatchIntent = viewModel::dispatchIntent,
        )
    }

    LibertyFlowScreen(
        navController = navController,
        viewModel = viewModel,
        pagingHandler = PagingHandler(
            items = catalog.loadState.refresh,
            dispatchIntent = { intent -> viewModel.dispatchIntent(HomeIntent.Catalog(action = intent)) }
        ),
    ) { dispatchIntent, _ ->
        HomeScreenScaffold(
            dispatchIntent = dispatchIntent,
            selectedIds = state.selection.ids,
            isInSelectionMode = state.selection.isInSelectionMode,
            searchState = state.search,
            loadingState = state.catalog.loading,
            isRefreshing = state.catalog.refreshing.isLoading,
            tile = state.tile,
            isRandomAnimeLoading = state.randomAnime.isLoading,
            items = catalog,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
