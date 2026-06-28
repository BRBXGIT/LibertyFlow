package com.brbx.home.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.compose.LazyPagingItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.view_model.model.intent.CommonSearchIntent
import com.brbx.common.view_model.model.state.CommonSearchState
import com.brbx.design_system.component.tile.TileModel
import com.brbx.design_system.component.top_bar.SearchableTopBar
import com.brbx.home.common.HomeStrings
import com.brbx.home.screen.content.Content
import com.brbx.home.screen.shimmer.ContentShimmer
import com.brbx.home.view_model.model.Intent
import com.brbx.mvi_compose.effects.BrbxEffect
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.containers.complex.scaffold.BrbxShimmerScaffold
import com.brbx.ui_compose.containers.complex.snackbar_host.BrbxSnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenScaffold(
    dispatchBrbxEffect: (BrbxEffect) -> Unit,
    dispatchIntent: (Intent) -> Unit,
    searchState: CommonSearchState,
    isShimmering: Boolean,
    tile: TileModel?,
    items: LazyPagingItems<AnimeItem>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    BrbxShimmerScaffold(
        topBar = {
            SearchableTopBar(
                onSearchClick =
                    { dispatchIntent(Intent.Search(action = CommonSearchIntent.ToggleSearching)) },
                onSystemBackClick =
                    { dispatchIntent(Intent.Search(action = CommonSearchIntent.ToggleSearching)) },
                onSearchChange =
                    { dispatchIntent(Intent.Search(action = CommonSearchIntent.UpdateSearch(it))) },
                isSearching = searchState.isSearching,
                title = HomeStrings.top_bar_title.toBrbxText(),
                search = searchState.search,
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { BrbxSnackbarHost() },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        isShimmering = isShimmering,
        shimmerContent = { paddingValues ->
            ContentShimmer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        },
        errorContent = {},
        content = { paddingValues ->
            Content(
                isSearching = searchState.isSearching,
                isRefreshing = isRefreshing,
                tile = tile,
                items = items,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        },
    )
}