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
import com.example.design_system.components.list_tems.AnimeCardUtils

// Common LazyVerticalGrid function
private val ArrangementAlignmentPadding = 16.dp

@Composable
fun PagingAnimeItemsLazyVerticalGrid(
    state: LazyGridState = rememberLazyGridState(),
    onItemClick: (Int) -> Unit,
    anime: LazyPagingItems<AnimeItem>,
    extraContent: LazyGridScope.() -> Unit = {}
) {
    // Paging grid — items loaded on-demand
    AnimeLazyVerticalGridContainer(state = state) {
        extraContent(this)

        items(
            count = anime.itemCount,
            key = { it }
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

@Composable
private fun AnimeLazyVerticalGridContainer(
    state: LazyGridState,
    content: LazyGridScope.() -> Unit,
) {
    // Shared grid container for both static and paging content
    LazyVerticalGrid(
        state = state,
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(AnimeCardUtils.CARD_WIDTH.dp),
        verticalArrangement = Arrangement.spacedBy(ArrangementAlignmentPadding),
        horizontalArrangement = Arrangement.spacedBy(ArrangementAlignmentPadding),
        contentPadding = PaddingValues(ArrangementAlignmentPadding),
        content = content
    )
}