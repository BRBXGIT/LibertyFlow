package com.brbx.common.composable.selection_menu

import androidx.annotation.StringRes
import com.brbx.common.model.alias.CommonStrings
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
) {
    AddToAnyList(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_add.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_add.toBrbxText(),
    ),
    DeleteFromFavoritesAddToCollection(
        favoritesIcon = OutlineSolar.Like.HeartBroken.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_remove.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.AddFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_add.toBrbxText(),
    ),
    DeleteFromCollectionAddToFavorites(
        favoritesIcon = OutlineSolar.Like.Heart.toBrbxIcon(),
        favoritesTextRes = CommonStrings.selection_menu_favorites_add.toBrbxText(),
        collectionsIcon = OutlineSolar.Folders.RemoveFolder.toBrbxIcon(),
        collectionsTextRes = CommonStrings.selection_menu_collections_remove.toBrbxText(),
    ),
}
