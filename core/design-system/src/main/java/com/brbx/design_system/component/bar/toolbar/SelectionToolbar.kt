package com.brbx.design_system.component.bar.toolbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    ) {
        SelectionActionGroup(
            modifier = Modifier
                .height(0.dp)
                .wrapContentHeight(align = Alignment.CenterVertically, unbounded = true),
            visible = isInSelectionMode,
            favoritesIcon = favoritesIcon,
            collectionsIcon = collectionsIcon,
            onFavoritesClick = onFavoritesClick,
            onCollectionsClick = onCollectionsClick,
        )

        BrbxToggleDisappearingFab(
            checked = isInSelectionMode,
            onCheckedChange = { onFabClick() },
            visible = isFabVisible,
            icon = fabIcon,
            checkedIcon = LibertyFlowIcons.Filled.Cross.toBrbxIcon(),
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
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = bMotion.nonSpatialExtraFastSpec()) +
                slideInHorizontally(
                    animationSpec = bMotion.fastSpatialSpec(),
                    initialOffsetX = { it / 2 },
                ),
        exit = fadeOut(animationSpec = bMotion.nonSpatialExtraFastSpec()) +
                slideOutHorizontally(
                    animationSpec = bMotion.fastSpatialSpec(),
                    targetOffsetX = { it / 2 },
                )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
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
                IconButton(
                    onClick = onFavoritesClick
                ) {
                    BrbxIcon(favoritesIcon)
                }
                IconButton(
                    onClick = onCollectionsClick
                ) {
                    BrbxIcon(collectionsIcon)
                }
            }

            Spacer(modifier = Modifier.width(bDimens.micro3))
        }
    }
}