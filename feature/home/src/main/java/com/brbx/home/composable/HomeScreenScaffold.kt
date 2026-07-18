package com.brbx.home.composable

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.composable.selection_menu.SelectionToolbar
import com.brbx.common.composable.selection_menu.SelectionType
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.Tile
import com.brbx.common.view_model.processor.loading.model.CommonLoadingState
import com.brbx.common.view_model.processor.search.model.CommonSearchIntent
import com.brbx.common.view_model.processor.search.model.CommonSearchState
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.design_system.component.nav_bar.state.rememberInsetsWithNavBar
import com.brbx.design_system.component.top_bar.SearchableTopBar
import com.brbx.design_system.container.ShimmerScaffold
import com.brbx.home.common.HomeStrings
import com.brbx.home.composable.content.HomeContent
import com.brbx.home.composable.shimmer.HomeContentShimmer
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
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
internal fun HomeScreenScaffold(
    dispatchIntent: (HomeIntent) -> Unit,
    selectedIds: Set<Int>,
    isInSelectionMode: Boolean,
    searchState: CommonSearchState,
    loadingState: CommonLoadingState,
    isRandomAnimeLoading: Boolean,
    tile: Tile?,
    items: LazyPagingItems<AnimeItem>,
    isRefreshing: Boolean,
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
        isShimmering = loadingState.isLoading,
        isError = loadingState.isException,
        snackbarHost = { BrbxSnackbarHost() },
        onShimmerEnd = { withError ->
            if (!withError) {
                dispatchIntent(HomeIntent.Tile.TogglePrecollectionVisibility)
            }
        },
        floatingActionButton = {
            HomeSelectionToolbar(
                scrollDirection = animeGridState.brbxScrollDirection(),
                loadingState = loadingState,
                isInSelectionMode = isInSelectionMode,
                dispatchIntent = dispatchIntent,
            )
        },
        topBar = {
            HomeTopBar(
                searchState = searchState,
                loadingState = loadingState,
                scrollBehavior = scrollBehavior,
                dispatchIntent = dispatchIntent,
            )
        },
        shimmerContent = { paddingValues ->
            HomeContentShimmer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        },
        content = { paddingValues ->
            HomeContent(
                isInSelectionMode = isInSelectionMode,
                selectedIds = selectedIds,
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
private fun HomeTopBar(
    searchState: CommonSearchState,
    loadingState: CommonLoadingState,
    scrollBehavior: TopAppBarScrollBehavior,
    dispatchIntent: (HomeIntent) -> Unit,
) {
    SearchableTopBar(
        onSearchClick = { dispatchIntent(HomeIntent.Search(action = CommonSearchIntent.ToggleSearching)) },
        onSystemBackClick = { dispatchIntent(HomeIntent.Search(action = CommonSearchIntent.ToggleSearching)) },
        onSearchChange = { dispatchIntent(HomeIntent.Search(action = CommonSearchIntent.UpdateSearch(it))) },
        isSearching = searchState.isSearching,
        searchIconEnabled = !loadingState.isException && !loadingState.isLoading,
        title = HomeStrings.top_bar_title.toBrbxText(),
        search = searchState.search,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun HomeSelectionToolbar(
    scrollDirection: BrbxScrollDirection,
    loadingState: CommonLoadingState,
    isInSelectionMode: Boolean,
    dispatchIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFabVisible = remember(
        key1 = loadingState,
        key2 = isInSelectionMode,
        key3 = scrollDirection,
    ) {
        val scrollCondition = scrollDirection == BrbxScrollDirection.Up
        val loadingCondition = !loadingState.isLoading && !loadingState.isException
        isInSelectionMode || (loadingCondition && scrollCondition)
    }
    val selectionType = remember { SelectionType.AddToAnyList }
    SelectionToolbar(
        modifier = modifier,
        type = selectionType,
        isInSelectionMode = isInSelectionMode,
        onSelectionModeChange = { selecting ->
            if (!selecting) {
                dispatchIntent(
                    HomeIntent.Selection(action = CommonSelectionIntent.Selection.DropSelection)
                )
            }
        },
        isFabVisible = isFabVisible,
        onCollectionsClick = {
            dispatchIntent(HomeIntent.Selection(action = selectionType.collectionsIntent))
        },
        onFavoritesClick = {
            dispatchIntent(HomeIntent.Selection(action = selectionType.favoritesIntent))
        },
        onFabClick = { dispatchIntent(HomeIntent.Filters.ToggleSheet) },
        fabIcon = OutlineSolar.DesignTools.Filters.toBrbxIcon(),
    )
}

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenScaffoldPreview() {
    val mockItems = flowOf(PagingData.from(emptyList<AnimeItem>())).collectAsLazyPagingItems()
    val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    BrbxTheme(colorScheme = scheme) {
        HomeScreenScaffold(
            dispatchIntent = {},
            searchState = CommonSearchState(),
            isRandomAnimeLoading = false,
            tile = null,
            items = mockItems,
            isRefreshing = false,
            modifier = Modifier.fillMaxSize(),
            selectedIds = emptySet(),
            isInSelectionMode = false,
            loadingState = CommonLoadingState(),
        )
    }
}
