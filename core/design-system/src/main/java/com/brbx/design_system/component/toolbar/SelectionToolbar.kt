package com.brbx.design_system.component.toolbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import com.brbx.design_system.theme.LibertyFlowIcons
import com.brbx.ui_compose.common.BrbxIcon
import com.brbx.ui_compose.common.toBrbxIcon
import com.brbx.ui_compose.components.complex.fab.toggle_disappearing_fab.BrbxToggleDisappearingFab
import com.brbx.ui_compose.components.simple.icon.BrbxIcon
import com.brbx.ui_compose.theme.bDimens
import com.brbx.ui_compose.theme.bMotion
import com.brbx.ui_compose.theme.bShapes
import com.brbx.ui_compose.theme.mColors

@Composable
fun SelectionToolbar(
    isInSelectionMode: Boolean,
    isFabVisible: Boolean,
    favoritesIcon: BrbxIcon,
    collectionsIcon: BrbxIcon,
    fabIcon: BrbxIcon,
    onFavoritesClick: () -> Unit,
    onCollectionsClick: () -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(bDimens.micro3)
    ) {
        SelectionActionGroup(
            visible = isInSelectionMode,
            favoritesIcon = favoritesIcon,
            collectionsIcon = collectionsIcon,
            onFavoritesClick = onFavoritesClick,
            onCollectionsClick = onCollectionsClick
        )

        val crossIcon = remember { LibertyFlowIcons.Filled.Cross.toBrbxIcon() }

        BrbxToggleDisappearingFab(
            checked = isInSelectionMode,
            onCheckedChange = { onFabClick() },
            visible = isFabVisible,
            icon = fabIcon,
            checkedIcon = crossIcon,
        )
    }
}

@Composable
private fun SelectionActionGroup(
    visible: Boolean,
    favoritesIcon: BrbxIcon,
    collectionsIcon: BrbxIcon,
    onFavoritesClick: () -> Unit,
    onCollectionsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enterAlpha = bMotion.nonSpatialExtraFastSpec<Float>()
    val enterSlide = bMotion.fastSpatialSpec<IntOffset>()
    val exitAlpha = bMotion.nonSpatialExtraFastSpec<Float>()
    val exitSlide = bMotion.fastSpatialSpec<IntOffset>()

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = enterAlpha) +
                slideInHorizontally(animationSpec = enterSlide) { it / 2 },
        exit = fadeOut(animationSpec = exitAlpha) +
                slideOutHorizontally(animationSpec = exitSlide) { it / 2 },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = bDimens.micro4)
                .background(
                    color = mColors.surfaceContainerHigh,
                    shape = bShapes.macro2,
                ),
        ) {
            IconButton(onClick = onFavoritesClick) {
                BrbxIcon(favoritesIcon)
            }
            IconButton(onClick = onCollectionsClick) {
                BrbxIcon(collectionsIcon)
            }
        }
    }
}