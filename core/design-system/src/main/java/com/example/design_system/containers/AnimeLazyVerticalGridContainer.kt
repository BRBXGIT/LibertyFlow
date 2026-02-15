package com.example.design_system.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.common.PosterType
import com.example.data.models.common.anime_item.AnimeItem
import com.example.design_system.components.list_tems.AnimeCard
import com.example.design_system.components.list_tems.AnimeCardConstants
import com.example.design_system.theme.theme.mDimens

/**
 * A full-screen [LazyVerticalGrid] specifically designed to handle [LazyPagingItems].
 *
 * This component handles the lifecycle of paged data, ensuring that items are only
 * rendered once they are successfully loaded from the [anime] pager. It uses an
 * adaptive column strategy based on [AnimeCardConstants.CARD_WIDTH] to ensure
 * the grid looks great on all screen sizes.
 *
 * @param state The scroll state of the grid, useful for "scroll to top" actions or
 * scroll position persistence.
 * @param onItemClick Callback triggered when an [AnimeItem] card is tapped, passing the anime ID.
 * @param anime The stream of paged data to be displayed.
 * @param extraContent Optional [LazyGridScope] content (like headers or promotional
 * banners) to be displayed before the list of anime.
 */
@Composable
fun PagingAnimeItemsLazyVerticalGrid(
    state: LazyGridState = rememberLazyGridState(),
    onItemClick: (Int) -> Unit,
    anime: LazyPagingItems<AnimeItem>,
    extraContent: LazyGridScope.() -> Unit = {}
) {
    // Paging grid — items loaded on-demand
    AnimeLazyVerticalGridContainer(state = state) {
        // Renders any injected headers or additional content
        extraContent(this)

        items(
            count = anime.itemCount,
            key = { index -> index }
        ) { index ->
            val item = anime[index]

            // Show card only if item actually loaded
            item?.let {
                AnimeCard(
                    posterPath = it.poster.fullPath(PosterType.PREVIEW),
                    genresString = it.genresAsString(),
                    title = it.name.russian,
                    onCardClick = { onItemClick(it.id) }
                )
            }
        }
    }
}

/**
 * A private shared container that defines the visual styling and adaptive
 * column logic for all anime-related grids.
 *
 * @param state The [LazyGridState] to attach to the grid.
 * @param content The grid content (items, headers, etc.) to be rendered.
 */
@Composable
private fun AnimeLazyVerticalGridContainer(
    state: LazyGridState,
    content: LazyGridScope.() -> Unit,
) {
    // Shared grid container for both static and paging content
    LazyVerticalGrid(
        state = state,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(AnimeCardConstants.CARD_WIDTH.dp),
        verticalArrangement = Arrangement.spacedBy(mDimens.paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(mDimens.paddingMedium),
        contentPadding = PaddingValues(mDimens.paddingMedium),
        content = content
    )
}