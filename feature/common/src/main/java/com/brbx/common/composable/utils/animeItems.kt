package com.brbx.common.composable.utils

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.brbx.common.model.common.model.AnimeItem
import com.brbx.design_system.component.anime_card.AnimeCard
import com.brbx.design_system.component.anime_card.AnimeCardShimmer
import com.brbx.ui_compose.common.toBrbxText
import com.brbx.ui_compose.modifiers.brbxAnimateItem

inline fun LazyGridScope.animeItems(
    selectedIds: Set<Int>,
    items: LazyPagingItems<AnimeItem>,
    crossinline onItemClick: (id: Int) -> Unit,
    crossinline inItemLongClick: (id: Int) -> Unit,
    itemModifier: Modifier = Modifier,
) =
    items(
        count = items.itemCount,
        key = { index -> index },
    ) { index ->
        val current = items[index]
        current?.let { anime ->
            val id = current.id
            AnimeCard(
                selected = id in selectedIds,
                onClick = { onItemClick(id) },
                modifier = itemModifier.brbxAnimateItem(scope = this),
                posterPath = anime.posterPath.fullPreview(),
                description = anime.genresAsBrbxText(),
                title = anime.name.russian.toBrbxText(),
            )
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