package com.brbx.home.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.screen.LibertyFlowScreen
import com.brbx.common.screen.PagingHandler
import com.brbx.common.strings.asBrbxText
import com.brbx.common.view_model.view_model.postNetworkExceptionSnackbar
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.view_model.ViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.home(
    navController: NavController,
) = composable<HomeRoute> {
    val viewModel = koinViewModel<ViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val catalog = state.catalog.catalog.collectAsLazyPagingItems()

    val pagingHandler = remember {
        PagingHandler(
            loadState = catalog.loadState.refresh,
            onException = { isFirstLoading, exception ->
                if (isFirstLoading) {
                    viewModel.dispatchIntent(Intent.Catalog.SetLoadingException(true))
                } else {
                    viewModel.dispatchIntent(Intent.Catalog.SetRefreshingException(true))
                }
                viewModel.postNetworkExceptionSnackbar(exception.asBrbxText()) { catalog.retry() }
            },
            onLoading = { isFirstLoading, isLoading ->
                if (isFirstLoading) {
                    viewModel.dispatchIntent(Intent.Catalog.SetLoading(isLoading))
                } else {
                    viewModel.dispatchIntent(Intent.Catalog.SetRefreshing(isLoading))
                }
            },
        )
    }

    LibertyFlowScreen(
        pagingHandler = pagingHandler,
        navController = navController,
        viewModel = viewModel,
    ) { dispatchIntent, dispatchBrbxEffect ->

    }
}