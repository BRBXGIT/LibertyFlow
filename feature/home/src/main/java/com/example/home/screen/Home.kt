@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import com.example.common.ui_helpers.effects.UiEffect
import com.example.common.ui_helpers.search.SearchForm
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.components.bars.bottom_nav_bar.calculateNavBarSize
import com.example.design_system.components.bars.searching_top_bar.SearchingTopBar
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid
import com.example.design_system.containers.PagingStatesContainer
import com.example.design_system.containers.VibratingContainer
import com.example.design_system.theme.theme.mColors
import com.example.design_system.utils.ScrollDirection
import com.example.design_system.utils.rememberDirectionalScrollState
import com.example.home.R
import com.example.home.components.FiltersBS
import com.example.home.components.FiltersFAB
import com.example.home.components.RANDOM_BUTTON_KEY
import com.example.home.components.RandomAnimeButton

private val TopBarLabel = R.string.home_top_bar_label
private const val RETRY_LABEL = "Retry"

@Composable
internal fun Home(
    state: HomeState,
    anime: LazyPagingItems<AnimeItem>,
    snackbarHostState: SnackbarHostState,
    onIntent: (HomeIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Synchronize Paging library states with our HomeState flags
    PagingStatesContainer(
        items = anime,
        onLoadingChange = { onIntent(HomeIntent.SetLoading(it)) },
        onErrorChange = { onIntent(HomeIntent.SetError(it)) },
        onRetryRequest = { messageRes, retry ->
            onEffect(
                UiEffect.ShowSnackbarWithAction(
                    messageRes = messageRes.toInt(),
                    actionLabel = RETRY_LABEL,
                    action = retry
                )
            )
        }
    )

    val lazyGridState = rememberLazyGridState()
    val directionalScroll = rememberDirectionalScrollState(lazyGridState)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(bottom = calculateNavBarSize()),
        topBar = {
            SearchingTopBar(
                searchForm = SearchForm(state.filtersState.request.search, state.isSearching),
                label = stringResource(TopBarLabel),
                scrollBehavior = scrollBehavior,
                onToggleSearch = { onIntent(HomeIntent.ToggleSearching) },
                onQueryChange = { onIntent(HomeIntent.UpdateQuery(it)) },
            )
        },
        floatingActionButton = {
            FiltersFAB(
                visible = directionalScroll.scrollDirection == ScrollDirection.Up,
                onIntent = onIntent
            )
        }
    ) { innerPadding ->
        // Bottom Sheet for filters
        if (state.filtersState.isFiltersBSVisible) FiltersBS(state, onIntent)

        // Pull-to-refresh Wrapper
        VibratingContainer(
            isSearching = state.isSearching,
            isRefreshing = state.loadingState.isLoading,
            onRefresh = anime::refresh,
            modifier = Modifier
                .fillMaxSize()
                .background(mColors.background)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = calculateNavBarSize()
                )
        ) {
            MainContent(
                listState = lazyGridState,
                state = state,
                anime = anime,
                onIntent = onIntent,
                onEffect = onEffect
            )
        }
    }
}

@Composable
private fun MainContent(
    listState: LazyGridState,
    state: HomeState,
    anime: LazyPagingItems<AnimeItem>,
    onIntent: (HomeIntent) -> Unit,
    onEffect: (UiEffect) -> Unit
) {
    // Early return for error state to keep indentation low
    if (state.loadingState.isError) {
        ErrorSection()
        return
    }

    PagingAnimeItemsLazyVerticalGrid(
        state = listState,
        anime = anime,
        onItemClick = { onEffect(UiEffect.Navigate(AnimeDetailsRoute(it))) }
    ) {
        // Randomizer button as a header/top item
        item(
            key = RANDOM_BUTTON_KEY,
            span = { GridItemSpan(maxLineSpan) }
        ) {
            RandomAnimeButton(state, onIntent)
        }
    }
}