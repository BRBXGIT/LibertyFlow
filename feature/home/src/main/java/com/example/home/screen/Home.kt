@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.common.navigation.AnimeDetailsRoute
import com.example.common.ui_helpers.UiEffect
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.buttons.BasicFAB
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.design_system.theme.LibertyFlowIcons
import com.example.design_system.theme.mColors
import com.example.home.R
import com.example.home.components.FiltersBS
import com.example.home.components.RANDOM_BUTTON_KEY
import com.example.home.components.RandomAnimeButton

private val TopBarLabel = R.string.home_top_bar_label

@Composable
fun Home(
    homeState: HomeState,
    anime: LazyPagingItems<UiAnimeItem>,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Handle paging loading/error states
    PagingStatesContainer(
        items = anime,
        onLoadingChange = { onIntent(HomeIntent.SetLoading(it)) },
        onErrorChange = { onIntent(HomeIntent.SetError(it)) },
        onRetryRequest = { messageRes, retry ->
            onEffect(
                UiEffect.ShowSnackbar(
                    messageRes = messageRes.toInt(),
                    actionLabel = "Retry",
                    action = retry
                )
            )
        }
    )

    val topBarScrollBehaviour = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            // Top search bar with query input & actions
            SearchingTopBar(
                query = homeState.request.search,
                label = stringResource(TopBarLabel),
                isSearching = homeState.isSearching,
                isLoading = homeState.isLoading,
                scrollBehavior = topBarScrollBehaviour,
                onSearchChange = { onIntent(HomeIntent.ToggleSearching) },
                onQueryChange = { onIntent(HomeIntent.UpdateQuery(it)) },
            )
        },
        floatingActionButton = {
            BasicFAB(
                icon = LibertyFlowIcons.Filters,
                onClick = { onIntent(HomeIntent.ToggleFiltersBottomSheet) }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
    ) { innerPadding ->
        if (homeState.isFiltersBSVisible) {
            FiltersBS(homeState, onIntent)
        }

        // Pull-to-refresh container with vibration feedback
        VibratingContainer(
            isRefreshing = homeState.isLoading,
            onRefresh = { anime.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            // Main content logic depending on search mode + error state
            MainContent(
                isError = homeState.isError,
                isRandomAnimeLoading = homeState.isRandomAnimeLoading,
                onIntent = onIntent,
                onEffect = onEffect,
                anime = anime
            )
        }
    }
}

@Composable
private fun MainContent(
    isError: Boolean,
    isRandomAnimeLoading: Boolean,
    onIntent: (HomeIntent) -> Unit,
    onEffect: (UiEffect) -> Unit,
    anime: LazyPagingItems<UiAnimeItem>
) {
    when(isError) {
        true -> ErrorSection()
        false -> PagingAnimeItemsLazyVerticalGrid(
            anime = anime,
            onItemClick = { onEffect(UiEffect.Navigate(AnimeDetailsRoute(it))) }
        ) {
            item(
                key = RANDOM_BUTTON_KEY,
                span = { GridItemSpan(maxLineSpan) }
            ) {
                RandomAnimeButton(onIntent, isRandomAnimeLoading)
            }
        }
    }
}