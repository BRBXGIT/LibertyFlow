package com.brbx.common.composable.utils

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.design_system.component.anime_card.AnimeCard
import com.brbx.design_system.component.anime_card.AnimeCardShimmer
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem

fun LazyGridScope.animeItems(
    selectedIds: Set<Int>,
    items: LazyPagingItems<AnimeItem>,
    onItemClick: (id: Int) -> Unit,
    onItemLongClick: (id: Int) -> Unit,
    itemModifier: Modifier = Modifier,
) {
    items(
        count = items.itemCount,
        key = items.itemKey { it.id },
        contentType = items.itemContentType { "AnimeItem" },
    ) { index ->
        val current = items[index]
        current?.let { anime ->
            val id = current.id

            val currentOnClick by rememberUpdatedState(onItemClick)
            val currentOnLongClick by rememberUpdatedState(onItemLongClick)

            AnimeCard(
                selected = selectedIds.contains(id),
                onClick = remember(id) { { currentOnClick(id) } },
                onLongClick = remember(id) { { currentOnLongClick(id) } },
                modifier = itemModifier.brbxAnimateItem(scope = this),
                posterPath = anime.posterPath.fullPreview(),
                description = anime.genresAsBrbxText(),
                title = anime.name.russian.toBrbxText(),
            )
        }
    }
}

fun LazyGridScope.animeItemsShimmer(
    count: Int = 6,
    itemModifier: Modifier = Modifier,
) {
    items(
        count = count,
        key = { index -> index },
    ) {
        AnimeCardShimmer(
            modifier = itemModifier.brbxAnimateItem(scope = this)
        )
    }
}