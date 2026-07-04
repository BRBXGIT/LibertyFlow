package com.brbx.home.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.processor.search.model.CommonSearchState
import com.brbx.common.view_model.processor.tile.model.CommonTile
import com.brbx.common.view_model.processor.tile.model.CommonTileIntent
import com.brbx.design_system.component.nav_bar.state.rememberInsetsWithNavBar
import com.brbx.design_system.component.top_bar.SearchableTopBar
import com.brbx.design_system.container.ShimmerScaffold
import com.brbx.home.common.HomeStrings
import com.brbx.home.composable.content.Content
import com.brbx.home.composable.shimmer.ContentShimmer
import com.brbx.home.view_model.model.Intent
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.components.complex.disappearing_fab.BrbxDisappearingFab
import com.brbx.ui_compose.containers.complex.scaffold.BrbxShimmerScaffoldAppearances
import com.brbx.ui_compose.containers.complex.scaffold.rememberCopy
import com.brbx.ui_compose.containers.complex.snackbar_host.BrbxSnackbarHost
import com.brbx.ui_compose.state.BrbxScrollDirection
import com.brbx.ui_compose.state.brbxScrollDirection
import com.brbx.ui_compose.theme.BrbxTheme
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.DesignTools
import dev.chiksmedina.solar.outline.designtools.Filters
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenScaffold(
    dispatchIntent: (Intent) -> Unit,
    searchState: CommonSearchState,
    isLoading: Boolean,
    isRandomAnimeLoading: Boolean,
    tile: CommonTile?,
    items: LazyPagingItems<AnimeItem>,
    isRefreshing: Boolean,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val appearance = BrbxShimmerScaffoldAppearances.default.rememberCopy(
        contentWindowInsets = { rememberInsetsWithNavBar() },
    )
    val animeGridState = rememberLazyGridState()

    ShimmerScaffold(
        appearance = appearance,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        isShimmering = isLoading,
        snackbarHost = { BrbxSnackbarHost() },
        isError = isError,
        onShimmerEnd = { withError ->
            if (!withError) {
                dispatchIntent(Intent.Tile(action = CommonTileIntent.TogglePrecollectionVisibility))
            }
        },
        floatingActionButton = {
            Fab(
                isVisible = !isError && !isLoading && animeGridState.brbxScrollDirection() == BrbxScrollDirection.Up,
                onClick = { dispatchIntent(Intent.Filters.ToggleSheet) }
            )
        },
        topBar = {
            TopBar(
                searchState = searchState,
                isError = isError,
                isLoading = isLoading,
                scrollBehavior = scrollBehavior,
                dispatchIntent = dispatchIntent
            )
        },
        shimmerContent = { paddingValues ->
            ContentShimmer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        },
        content = { paddingValues ->
            Content(
                animeGridState = animeGridState,
                isSearching = searchState.isSearching,
                isRefreshing = isRefreshing,
                tile = tile,
                items = items,
                dispatchIntent = dispatchIntent,
                isRandomAnimeLoading = isRandomAnimeLoading,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    searchState: CommonSearchState,
    isError: Boolean,
    isLoading: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    dispatchIntent: (Intent) -> Unit,
) {
    SearchableTopBar(
        onSearchClick = { dispatchIntent(Intent.Search(action = CommonSearchIntent.ToggleSearching)) },
        onSystemBackClick = { dispatchIntent(Intent.Search(action = CommonSearchIntent.ToggleSearching)) },
        onSearchChange = { dispatchIntent(Intent.Search(action = CommonSearchIntent.UpdateSearch(it))) },
        isSearching = searchState.isSearching,
        searchIconEnabled = !isError && !isLoading,
        title = HomeStrings.top_bar_title.toBrbxText(),
        search = searchState.search,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun Fab(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    BrbxDisappearingFab(
        visible = isVisible,
        onClick = onClick,
        icon = OutlineSolar.DesignTools.Filters.toBrbxIcon(),
    )
}

@Preview(showBackground = true)
@Composable
private fun ScreenScaffoldPreview() {
    val mockItems = flowOf(PagingData.from(emptyList<AnimeItem>())).collectAsLazyPagingItems()
    BrbxTheme(colorScheme = lightColorScheme()) {
        ScreenScaffold(
            dispatchIntent = {},
            searchState = CommonSearchState(),
            isLoading = false,
            isRandomAnimeLoading = false,
            tile = null,
            items = mockItems,
            isRefreshing = false,
            isError = false,
            modifier = Modifier.fillMaxSize()
        )
    }
}
