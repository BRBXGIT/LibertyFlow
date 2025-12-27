package com.example.collections.components

import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.example.data.models.common.ui_anime_item.UiAnimeItem
import com.example.design_system.components.sections.ErrorSection
import com.example.design_system.containers.PagingAnimeItemsLazyVerticalGrid

private const val ZERO_ELEMENTS = 0

/**
 * Displays a single collection page.
 *
 * This function is marked as internal because it is used
 * only within the collections feature module.
 */
@Composable
internal fun CollectionPage(
    isError: Boolean,
    collection: LazyPagingItems<UiAnimeItem>,
    onItemClick: (Int) -> Unit,
) {
    when {
        // Global error state
        isError -> ErrorSection()

        // Empty collection
        collection.itemCount == ZERO_ELEMENTS -> {
            EmptyCollectionSection()
        }

        // Successfully loaded list
        else -> PagingAnimeItemsLazyVerticalGrid(
            anime = collection,
            onItemClick = onItemClick
        )
    }
}