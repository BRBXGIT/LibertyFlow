package com.brbx.home.composable.content

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.brbx.common.composable.utils.animeItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.common.model.common.model.Genre
import com.brbx.common.model.common.model.Name
import com.brbx.common.model.common.model.Poster
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.tile.model.CommonTile
import com.brbx.common.view_model.processor.tile.model.TileType
import com.brbx.design_system.component.rainbow_button.RainbowButton
import com.brbx.design_system.component.tile.Tile
import com.brbx.design_system.container.AnimeItemsLazyVerticalGrid
import com.brbx.design_system.container.PullToRefreshContainer
import com.brbx.home.common.HomeStrings
import com.brbx.home.view_model.model.HomeIntent
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem
import com.brbx.ui_compose.theme.BrbxTheme
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.FacesEmotionsStickers
import dev.chiksmedina.solar.outline.facesemotionsstickers.EmojiFunnySquare
import kotlinx.coroutines.flow.flowOf

/**
 * Main content of the Home screen.
 *
 * @param animeGridState State of the anime grid.
 * @param tile Current tile data (e.g. latest watched episode).
 * @param items Lazy paging items for the anime catalog.
 * @param isRefreshing Whether the content is currently refreshing.
 * @param isSearching Whether the user is currently searching.
 * @param selectedIds Set of selected anime IDs.
 * @param isInSelectionMode Whether the screen is in selection mode.
 * @param isRandomAnimeLoading Whether a random anime is being fetched.
 * @param dispatchIntent Function to dispatch intents.
 * @param modifier Modifier to be applied to the container.
 */
@Composable
internal fun HomeContent(
    animeGridState: LazyGridState,
    tile: CommonTile?,
    items: LazyPagingItems<AnimeItem>,
    isRefreshing: Boolean,
    isSearching: Boolean,
    selectedIds: Set<Int>,
    isInSelectionMode: Boolean,
    isRandomAnimeLoading: Boolean,
    dispatchIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshContainer(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = items::refresh,
        minimalisticIndicator = isSearching,
    ) {
        AnimeItemsLazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = animeGridState,
            userScrollEnabled = !isRefreshing,
        ) {
            if (!isSearching) {
                randomAnimeButton(
                    isLoading = isRandomAnimeLoading,
                    onClick = { dispatchIntent(HomeIntent.GetRandomAnime) }
                )

                tileItem(tile = tile)
            }

            animeItems(
                items = items,
                onItemClick = { id ->
                    if (isInSelectionMode) {
                        dispatchIntent(getSelectionIntent(id))
                    } else {
                        /* TODO Navigate to details */
                    }
                },
                selectedIds = selectedIds,
                onItemLongClick = { id -> dispatchIntent(getSelectionIntent(id)) },
            )
        }
    }
}

private fun LazyGridScope.randomAnimeButton(
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    item(
        key = HomeContentKeys.RandomAnimeButtonKey,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        RainbowButton(
            text = HomeStrings.random_anime_button_text.toBrbxText(),
            showAnimation = isLoading,
            icon = OutlineSolar.FacesEmotionsStickers.EmojiFunnySquare.toBrbxIcon(),
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .brbxAnimateItem(scope = this)
        )
    }
}

private fun LazyGridScope.tileItem(tile: CommonTile?) {
    if (tile == null) return
    item(
        key = HomeContentKeys.Tile,
        span = { GridItemSpan(currentLineSpan = maxLineSpan) },
    ) {
        val onTileClick: () -> Unit = remember(tile) { {} }

        Tile(
            title = tile.title,
            description = tile.description,
            icon = tile.icon,
            onTileClick = onTileClick,
            isPrecollectionVisible = tile.isPrecollectionVisible,
            precollectionIcon = tile.precollection?.icon,
            precollectionText = tile.precollection?.label,
            onPrecollectionClick = onTileClick,
            modifier = Modifier.brbxAnimateItem(scope = this)
        )
    }
}

private fun getSelectionIntent(id: Int): HomeIntent.Selection =
    HomeIntent.Selection(action = CommonSelectionIntent.Selection.ToggleItemSelected(id))

@Preview(name = "Light Theme", showBackground = true)
@Preview(name = "Dark Theme", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    val mockItems = flowOf(
        PagingData.from(
            listOf(
                AnimeItem(
                    id = 1,
                    name = Name(russian = "Атака титанов", english = "Attack on Titan", alternative = null),
                    genres = listOf(Genre(id = 1, name = "Экшен")),
                    posterPath = Poster(preview = "", src = "", thumbnail = "")
                ),
                AnimeItem(
                    id = 2,
                    name = Name(russian = "Наруто", english = "Naruto", alternative = null),
                    genres = listOf(Genre(id = 2, name = "Приключения")),
                    posterPath = Poster(preview = "", src = "", thumbnail = "")
                )
            )
        )
    ).collectAsLazyPagingItems()
    val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    BrbxTheme(colorScheme = scheme) {
        HomeContent(
            animeGridState = rememberLazyGridState(),
            tile = CommonTile(
                type = TileType.Episode.LatestWatched,
                title = "Продолжить просмотр".toBrbxText(),
                description = "Серия 12".toBrbxText(),
                icon = OutlineSolar.FacesEmotionsStickers.EmojiFunnySquare.toBrbxIcon(),
                precollection = null,
                isPrecollectionVisible = false,
            ),
            items = mockItems,
            isRefreshing = false,
            isSearching = false,
            isRandomAnimeLoading = false,
            dispatchIntent = {},
            selectedIds = emptySet(),
            isInSelectionMode = false,
        )
    }
}
