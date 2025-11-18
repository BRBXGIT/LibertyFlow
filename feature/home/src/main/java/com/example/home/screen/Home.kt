@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.home.screen

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.design_system.components.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.searching_top_bar.SearchingTopBar
import com.example.design_system.components.sections.EmptyQuerySection
import com.example.design_system.components.snackbars.getSnackbarState
import com.example.design_system.components.snackbars.sendRetrySnackbar
import com.example.design_system.containers.AnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import kotlinx.coroutines.launch

@Composable
fun Home(
    homeState: HomeState,
    animeByQuery: LazyPagingItems<UiAnimeItem>,
    onIntent: (HomeIntent) -> Unit
) {
    val snackbars = getSnackbarState()

    PagingStatesContainer(
        items = animeByQuery,
        onLoadingChange = { onIntent(HomeIntent.UpdateIsLoading(it)) },
        onRetryRequest = { message, retry ->
            if (homeState.query.isNotBlank()) {
                snackbars.snackbarScope.launch { sendRetrySnackbar(message, retry) }
            }
        }
    )

    val topBarScrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        snackbarHost = { SnackbarHost(hostState = snackbars.snackbarHostState) },
        modifier = Modifier.fillMaxSize().nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
        topBar = {
            SearchingTopBar(
                query = homeState.query,
                scrollBehavior = topBarScrollBehaviour,
                label = stringResource(HomeUtils.topBarLabel),
                isSearching = homeState.isSearching,
                onSearchChange = { onIntent(HomeIntent.UpdateIsSearching) },
                onQueryChange = { onIntent(HomeIntent.UpdateQuery(it)) },
                isLoading = homeState.isLoading
            )
        }
    ) { innerPadding ->
        VibratingContainer(
            topPadding = innerPadding.calculateTopPadding(),
            isRefreshing = homeState.isLoading,
            onRefresh = { animeByQuery.refresh() }
        ) {
            when(homeState.isSearching) {
                false -> AnimeItemsLazyVerticalGrid(homeState.latestReleases)
                true -> {
                    if (homeState.query.isBlank()) {
                        EmptyQuerySection()
                    } else {
                        PagingAnimeItemsLazyVerticalGrid(animeByQuery)
                    }
                }
            }
        }
    }
}