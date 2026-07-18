package com.brbx.common.composable.selection_menu

import com.brbx.common.model.alias.CommonStrings
import com.brbx.common.view_model.processor.selection.model.CommonSelectionIntent
import com.brbx.common.view_model.processor.selection.model.SelectionAction
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.BrbxText
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.common.toBrbxText
import dev.chiksmedina.solar.OutlineSolar
import dev.chiksmedina.solar.outline.Folders
import dev.chiksmedina.solar.outline.Like
import dev.chiksmedina.solar.outline.folders.AddFolder
import dev.chiksmedina.solar.outline.folders.RemoveFolder
import dev.chiksmedina.solar.outline.like.Heart
import dev.chiksmedina.solar.outline.like.HeartBroken

enum class SelectionType(
    val collectionsTextRes: BrbxText,
    val favoritesTextRes: BrbxText,
    val favoritesIcon: BrbxIcon,
    val collectionsIcon: BrbxIcon,
    val favoritesIntent: CommonSelectionIntent.Lists.Favorites,
    val collectionsIntent: CommonSelectionIntent.Lists.Collection,
) {
    AddToAnyList(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_add.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_add.toBrbxText(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Add),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
    DeleteFromFavoritesAddToCollection(
        favoritesIcon = OutlineSolar.Like.HeartBroken.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_remove.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_add.toBrbxText(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Delete),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
    DeleteFromCollectionAddToFavorites(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_add.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.RemoveFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_remove.toBrbxText(),
        favoritesIntent = CommonSelectionIntent.Lists.Favorites(action = SelectionAction.Add),
        collectionsIntent = CommonSelectionIntent.Lists.Collection.ToggleSheet,
    ),
}
