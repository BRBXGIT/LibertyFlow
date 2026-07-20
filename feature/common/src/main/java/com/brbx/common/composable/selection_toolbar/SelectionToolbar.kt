package com.brbx.common.composable.selection_toolbar

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.design_system.component.toolbar.SelectionToolbar
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
            dispatchIntent(CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Add))
        },
        onCollectionsClick = {
            dispatchIntent(CommonSelectionIntent.Lists.Collection.ToggleSheet)
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