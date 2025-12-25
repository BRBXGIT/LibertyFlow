package com.example.collections.components

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.collections.components.CollectionPageConstants.ZERO_ELEMENTS
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid

private object CollectionPageConstants {
    const val ZERO_ELEMENTS = 0
}

@Composable
internal fun CollectionPage(
    isError: Boolean,
    collection: LazyPagingItems<UiAnimeItem>,
    onItemClick: (Int) -> Unit,
) {
    when {
        isError -> ErrorSection()
        collection.itemCount == ZERO_ELEMENTS -> EmptyCollectionSection()
        else -> PagingAnimeItemsLazyVerticalGrid(
            anime = collection,
            onItemClick = { onItemClick(it) }
        )
    }
}