@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.home.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.components.snackbars.getSnackbarState
import com.example.design_system.components.snackbars.sendRetrySnackbar
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.home.R
import com.example.home.components.RandomAnimeButton
import com.example.home.components.RandomAnimeButtonConstants
import kotlinx.coroutines.launch

private object HomeConstants {
    val topBarLabel = R.string.top_bar_label
}

@Composable
fun Home(
    homeState: HomeState,
    anime: LazyPagingItems<UiAnimeItem>,
    onIntent: (HomeIntent) -> Unit
) {
    // Snackbar controller
    val snackbars = getSnackbarState()

    // Handle paging loading/error states
    PagingStatesContainer(
        items = anime,
        onLoadingChange = { onIntent(HomeIntent.UpdateIsLoading(it)) },
        onErrorChange = { onIntent(HomeIntent.UpdateIsError(it)) },
        onRetryRequest = { message, retry ->
            snackbars.snackbarScope.launch {
                sendRetrySnackbar(message, retry)
            }
        }
    )

    val topBarScrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbars.snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            // Top search bar with query input & actions
            SearchingTopBar(
                query = homeState.request.search,
                label = stringResource(HomeConstants.topBarLabel),
                isSearching = homeState.isSearching,
                isLoading = homeState.isLoading,
                scrollBehavior = topBarScrollBehaviour,
                onSearchChange = { onIntent(HomeIntent.UpdateIsSearching) },
                onQueryChange = { onIntent(HomeIntent.UpdateQuery(it)) },
            )
        }
    ) { innerPadding ->
        // Pull-to-refresh container with vibration feedback
        VibratingContainer(
            topPadding = innerPadding.calculateTopPadding(),
            isRefreshing = homeState.isLoading,
            onRefresh = { anime.refresh() }
        ) {
            // Main content logic depending on search mode + error state
            when(homeState.isError) {
                true -> ErrorSection()
                false -> PagingAnimeItemsLazyVerticalGrid(anime) {
                    item(
                        key = RandomAnimeButtonConstants.RANDOM_BUTTON_KEY,
                        span = { GridItemSpan(maxLineSpan) }
                    ) {
                        RandomAnimeButton(onClick = { onIntent(HomeIntent.GetRandomAnime) })
                    }
                }
            }
        }
    }
}