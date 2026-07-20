package com.brbx.common.composable.selection_toolbar

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.design_system.component.bar.toolbar.SelectionToolbar
import com.brbx.ui_compose.common.BrbxIcon

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CommonSelectionToolbar(
    type: SelectionType,
    isInSelectionMode: Boolean,
    isFabVisible: Boolean,
    dispatchIntent: (CommonSelectionIntent) -> Unit,
    onFabClick: () -> Unit,
    fabIcon: BrbxIcon,
    modifier: Modifier = Modifier,
) =
    SelectionToolbar(
        isInSelectionMode = isInSelectionMode,
        isFabVisible = isFabVisible,
        favoritesIcon = type.favoritesIcon,
        collectionsIcon = type.collectionsIcon,
        fabIcon = fabIcon,
        onFavoritesClick = {
            dispatchIntent(type.favoritesIntent)
        },
        onCollectionsClick = {
            dispatchIntent(type.collectionsIntent)
        },
        onFabClick = {
            if (isInSelectionMode) {
                dispatchIntent(CommonSelectionIntent.Selection.DropSelection)
            } else {
                onFabClick()
            }
        },
        modifier = modifier
    )