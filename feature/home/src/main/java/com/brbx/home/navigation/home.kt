package com.brbx.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.composable.bottom_sheet.auth_sheet.CommonAuthSheet
import com.brbx.common.composable.bottom_sheet.collections_sheet.CollectionsSheet
import com.brbx.common.composable.screen.LibertyFlowScreen
import com.brbx.common.composable.screen.PagingHandler
import com.brbx.common.composable.selection_toolbar.collectionsInteractionIntent
import com.brbx.common.view_model.processor.auth.model.CommonAuthSheetIntent
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.view_model.model.LibertyFlowCommonEffect
import com.brbx.home.common.HomeConstants
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

    val dispatchIntent = viewModel::dispatchIntent

    HomeFiltersSheet(
        filters = state.filtersSheet,
        dispatchIntent = dispatchIntent,
    )

    CommonAuthSheet(
        authSheetState = state.authSheetState,
        dispatchCommonEffect = viewModel::dispatchCommonEffect,
        dispatchIntent = { authIntent ->
            dispatchIntent(HomeIntent.AuthSheet(action = authIntent))
        },
    )

    CollectionsSheet(
        visible = state.selection.isCollectionsSheetVisible,
        onItemClick = { collection ->
            val selectionIntent = HomeConstants
                .ToolbarSelectionType
                .collectionsInteractionIntent(collection)
            dispatchIntent(HomeIntent.Selection(action = selectionIntent))
        },
        onDismissRequest = {
            dispatchIntent(
                HomeIntent.Selection(
                    action = CommonSelectionIntent.Lists.Collection.ToggleSheet
                )
            )
        },
    )

    LibertyFlowScreen(
        navController = navController,
        viewModel = viewModel,
        pagingHandler = PagingHandler(
            items = catalog.loadState.refresh,
            dispatchIntent = { intent -> viewModel.dispatchIntent(HomeIntent.Catalog(action = intent)) }
        ),
        onCustomEffect = { effect ->
            when (effect) {
                is LibertyFlowCommonEffect.RequireAuth -> {
                    dispatchIntent(HomeIntent.AuthSheet(action = CommonAuthSheetIntent.ToggleSheet))
                }
            }
        }
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
