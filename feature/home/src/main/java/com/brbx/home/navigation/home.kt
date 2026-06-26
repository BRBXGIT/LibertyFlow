package com.brbx.home.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.screen.LibertyFlowScreen
import com.brbx.common.screen.PagingHandler
import com.brbx.home.view_model.model.Intent
import com.brbx.home.view_model.view_model.ViewModel
import com.brbx.ui_compose.theme.mColors
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.home(
    navController: NavController,
) = composable<HomeRoute> {
    val viewModel = koinViewModel<ViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val catalog = state.catalog.pagingData.collectAsLazyPagingItems()

    LibertyFlowScreen(
        navController = navController,
        viewModel = viewModel,
        pagingHandler = PagingHandler(
            loadState = catalog.loadState.refresh,
            dispatchIntent = { intent -> viewModel.dispatchIntent(Intent.Catalog(action = intent)) }
        ),
    ) { dispatchIntent, dispatchBrbxEffect ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.primary)
        ) {
            items(catalog.itemCount) {
                val item = catalog[it]
                item?.let { i ->
                    Text(
                        text = i.id.toString(),
                        color = mColors.onPrimary,
                    )
                }
            }
        }
    }
}