package com.brbx.common.composable.selection_toolbar

import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.toBrbxIcon
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.Folders
import dev.chiksmedina.solar.outline.Like
import dev.chiksmedina.solar.outline.folders.AddFolder
import dev.chiksmedina.solar.outline.folders.RemoveFolder
import dev.chiksmedina.solar.outline.like.Heart
import dev.chiksmedina.solar.outline.like.HeartBroken

enum class SelectionType(
    val favoritesIcon: BrbxIcon,
    val collectionsIcon: BrbxIcon,
    val favoritesIntent: CommonSelectionIntent.Lists.Favorites,
    val collectionsIntent: CommonSelectionIntent.Lists.Collection,
) {
    AddToAnyList(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Add),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
    DeleteFromFavoritesAddToCollection(
        favoritesIcon = OutlineSolar.Like.HeartBroken.toBrbxIcon(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Delete),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
    DeleteFromCollectionAddToFavorites(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        collectionsIcon = OutlineSolar.Folders.RemoveFolder.toBrbxIcon(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Add),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
}
